#!/bin/bash

# Java开发环境设置脚本
# 用于快速设置Java开发环境

set -e

echo "🚀 开始设置Java开发环境..."

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 检查系统
check_system() {
    echo -e "${BLUE}检查系统信息...${NC}"
    echo "操作系统: $(uname -s)"
    echo "架构: $(uname -m)"
}

# 安装Java
install_java() {
    echo -e "${BLUE}安装Java...${NC}"
    
    if command -v java &> /dev/null; then
        echo -e "${GREEN}Java已安装: $(java -version 2>&1 | head -n 1)${NC}"
    else
        echo -e "${YELLOW}安装Java 17...${NC}"
        if [[ "$OSTYPE" == "linux-gnu"* ]]; then
            sudo apt update
            sudo apt install -y openjdk-17-jdk
        elif [[ "$OSTYPE" == "darwin"* ]]; then
            brew install openjdk@17
        fi
    fi
}

# 安装Maven
install_maven() {
    echo -e "${BLUE}安装Maven...${NC}"
    
    if command -v mvn &> /dev/null; then
        echo -e "${GREEN}Maven已安装: $(mvn -version | head -n 1)${NC}"
    else
        echo -e "${YELLOW}安装Maven...${NC}"
        if [[ "$OSTYPE" == "linux-gnu"* ]]; then
            sudo apt install -y maven
        elif [[ "$OSTYPE" == "darwin"* ]]; then
            brew install maven
        fi
    fi
}

# 安装Gradle
install_gradle() {
    echo -e "${BLUE}安装Gradle...${NC}"
    
    if command -v gradle &> /dev/null; then
        echo -e "${GREEN}Gradle已安装: $(gradle -version | head -n 1)${NC}"
    else
        echo -e "${YELLOW}安装Gradle...${NC}"
        if [[ "$OSTYPE" == "linux-gnu"* ]]; then
            sudo apt install -y gradle
        elif [[ "$OSTYPE" == "darwin"* ]]; then
            brew install gradle
        fi
    fi
}

# 安装Git
install_git() {
    echo -e "${BLUE}安装Git...${NC}"
    
    if command -v git &> /dev/null; then
        echo -e "${GREEN}Git已安装: $(git --version)${NC}"
    else
        echo -e "${YELLOW}安装Git...${NC}"
        if [[ "$OSTYPE" == "linux-gnu"* ]]; then
            sudo apt install -y git
        elif [[ "$OSTYPE" == "darwin"* ]]; then
            brew install git
        fi
    fi
}

# 安装Docker
install_docker() {
    echo -e "${BLUE}安装Docker...${NC}"
    
    if command -v docker &> /dev/null; then
        echo -e "${GREEN}Docker已安装: $(docker --version)${NC}"
    else
        echo -e "${YELLOW}安装Docker...${NC}"
        if [[ "$OSTYPE" == "linux-gnu"* ]]; then
            curl -fsSL https://get.docker.com -o get-docker.sh
            sudo sh get-docker.sh
            sudo usermod -aG docker $USER
            rm get-docker.sh
        elif [[ "$OSTYPE" == "darwin"* ]]; then
            echo "请从 https://www.docker.com/products/docker-desktop 下载Docker Desktop"
        fi
    fi
}

# 设置环境变量
setup_environment() {
    echo -e "${BLUE}设置环境变量...${NC}"
    
    JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
    
    echo "export JAVA_HOME=$JAVA_HOME" >> ~/.bashrc
    echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> ~/.bashrc
    
    echo -e "${GREEN}环境变量设置完成${NC}"
}

# 创建项目结构
create_project_structure() {
    echo -e "${BLUE}创建项目目录结构...${NC}"
    
    # 创建示例项目
    if [ ! -d "projects/sample-project" ]; then
        mkdir -p projects/sample-project
        cp -r templates/maven-template/* projects/sample-project/
        echo -e "${GREEN}示例项目创建完成${NC}"
    fi
}

# 主函数
main() {
    echo -e "${GREEN}Java开发环境设置脚本${NC}"
    echo "================================"
    
    check_system
    install_java
    install_maven
    install_gradle
    install_git
    install_docker
    setup_environment
    create_project_structure
    
    echo "================================"
    echo -e "${GREEN}✅ Java开发环境设置完成！${NC}"
    echo -e "${YELLOW}请重启终端或运行 'source ~/.bashrc' 来应用环境变量${NC}"
}

# 运行主函数
main "$@"