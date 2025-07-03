#!/bin/bash

# 创建新Java项目脚本

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 显示帮助信息
show_help() {
    echo "Java项目创建工具"
    echo ""
    echo "用法: $0 [选项] <项目名称>"
    echo ""
    echo "选项:"
    echo "  -t, --type TYPE    项目类型 (maven|gradle|spring-boot)"
    echo "  -g, --group GROUP  组织ID (默认: com.example)"
    echo "  -v, --version VER  版本号 (默认: 1.0.0)"
    echo "  -h, --help        显示帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 -t maven my-app"
    echo "  $0 -t spring-boot -g com.mycompany my-web-app"
}

# 默认值
PROJECT_TYPE="maven"
GROUP_ID="com.example"
VERSION="1.0.0"
PROJECT_NAME=""

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        -t|--type)
            PROJECT_TYPE="$2"
            shift 2
            ;;
        -g|--group)
            GROUP_ID="$2"
            shift 2
            ;;
        -v|--version)
            VERSION="$2"
            shift 2
            ;;
        -h|--help)
            show_help
            exit 0
            ;;
        *)
            if [[ -z "$PROJECT_NAME" ]]; then
                PROJECT_NAME="$1"
            fi
            shift
            ;;
    esac
done

# 检查项目名称
if [[ -z "$PROJECT_NAME" ]]; then
    echo -e "${RED}错误: 请提供项目名称${NC}"
    show_help
    exit 1
fi

# 验证项目类型
if [[ ! "$PROJECT_TYPE" =~ ^(maven|gradle|spring-boot)$ ]]; then
    echo -e "${RED}错误: 不支持的项目类型 '$PROJECT_TYPE'${NC}"
    echo "支持的类型: maven, gradle, spring-boot"
    exit 1
fi

# 创建项目
create_project() {
    local project_dir="projects/$PROJECT_NAME"
    
    echo -e "${BLUE}创建 $PROJECT_TYPE 项目: $PROJECT_NAME${NC}"
    
    # 检查项目是否已存在
    if [[ -d "$project_dir" ]]; then
        echo -e "${RED}错误: 项目 '$PROJECT_NAME' 已存在${NC}"
        exit 1
    fi
    
    # 创建项目目录
    mkdir -p "$project_dir"
    
    # 复制模板
    case $PROJECT_TYPE in
        maven)
            cp -r templates/maven-template/* "$project_dir/"
            customize_maven_project "$project_dir"
            ;;
        gradle)
            cp -r templates/gradle-template/* "$project_dir/"
            customize_gradle_project "$project_dir"
            ;;
        spring-boot)
            cp -r templates/spring-boot-template/* "$project_dir/"
            customize_spring_boot_project "$project_dir"
            ;;
    esac
    
    echo -e "${GREEN}✅ 项目创建成功: $project_dir${NC}"
    echo -e "${YELLOW}下一步:${NC}"
    echo "  cd $project_dir"
    
    if [[ "$PROJECT_TYPE" == "maven" || "$PROJECT_TYPE" == "spring-boot" ]]; then
        echo "  mvn compile"
        echo "  mvn test"
    else
        echo "  ./gradlew build"
        echo "  ./gradlew test"
    fi
}

# 自定义Maven项目
customize_maven_project() {
    local project_dir="$1"
    
    # 更新pom.xml
    sed -i "s/com\.example/$GROUP_ID/g" "$project_dir/pom.xml"
    sed -i "s/java-project-template/$PROJECT_NAME/g" "$project_dir/pom.xml"
    sed -i "s/1\.0\.0/$VERSION/g" "$project_dir/pom.xml"
    
    # 创建包目录结构
    local package_dir=$(echo $GROUP_ID | sed 's/\./\//g')
    mkdir -p "$project_dir/src/main/java/$package_dir"
    mkdir -p "$project_dir/src/test/java/$package_dir"
    
    # 移动Java文件到正确的包目录
    if [[ -f "$project_dir/src/main/java/com/example/Main.java" ]]; then
        mv "$project_dir/src/main/java/com/example/"* "$project_dir/src/main/java/$package_dir/"
        rmdir -p "$project_dir/src/main/java/com/example" 2>/dev/null || true
    fi
    
    if [[ -f "$project_dir/src/test/java/com/example/CalculatorTest.java" ]]; then
        mv "$project_dir/src/test/java/com/example/"* "$project_dir/src/test/java/$package_dir/"
        rmdir -p "$project_dir/src/test/java/com/example" 2>/dev/null || true
    fi
    
    # 更新Java文件中的包名
    find "$project_dir/src" -name "*.java" -exec sed -i "s/package com\.example/package $GROUP_ID/g" {} \;
}

# 自定义Gradle项目
customize_gradle_project() {
    local project_dir="$1"
    
    # 更新build.gradle
    sed -i "s/com\.example/$GROUP_ID/g" "$project_dir/build.gradle"
    sed -i "s/1\.0\.0/$VERSION/g" "$project_dir/build.gradle"
    
    # 创建gradlew脚本
    if [[ ! -f "$project_dir/gradlew" ]]; then
        cd "$project_dir"
        gradle wrapper
        cd - > /dev/null
    fi
    
    customize_maven_project "$project_dir"  # 复用Java文件处理逻辑
}

# 自定义Spring Boot项目
customize_spring_boot_project() {
    local project_dir="$1"
    
    # 更新pom.xml
    sed -i "s/com\.example/$GROUP_ID/g" "$project_dir/pom.xml"
    sed -i "s/spring-boot-template/$PROJECT_NAME/g" "$project_dir/pom.xml"
    sed -i "s/1\.0\.0/$VERSION/g" "$project_dir/pom.xml"
    
    # 创建包目录结构
    local package_dir=$(echo $GROUP_ID | sed 's/\./\//g')
    mkdir -p "$project_dir/src/main/java/$package_dir"
    mkdir -p "$project_dir/src/main/java/$package_dir/controller"
    mkdir -p "$project_dir/src/test/java/$package_dir"
    
    # 移动Java文件
    if [[ -f "$project_dir/src/main/java/com/example/Application.java" ]]; then
        mv "$project_dir/src/main/java/com/example/Application.java" "$project_dir/src/main/java/$package_dir/"
        mv "$project_dir/src/main/java/com/example/controller/"* "$project_dir/src/main/java/$package_dir/controller/"
        rmdir -p "$project_dir/src/main/java/com/example/controller" 2>/dev/null || true
        rmdir -p "$project_dir/src/main/java/com/example" 2>/dev/null || true
    fi
    
    # 更新Java文件中的包名
    find "$project_dir/src" -name "*.java" -exec sed -i "s/package com\.example/package $GROUP_ID/g" {} \;
    find "$project_dir/src" -name "*.java" -exec sed -i "s/com\.example\.controller/$GROUP_ID.controller/g" {} \;
}

# 主函数
main() {
    echo -e "${GREEN}Java项目创建工具${NC}"
    echo "=========================="
    echo "项目名称: $PROJECT_NAME"
    echo "项目类型: $PROJECT_TYPE"
    echo "组织ID: $GROUP_ID"
    echo "版本: $VERSION"
    echo "=========================="
    
    create_project
}

# 运行主函数
main