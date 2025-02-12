# ---- Etapa de Build ----
# Use a imagem oficial do Maven com suporte ao JDK 21 para construir a aplicação
FROM maven:3.9-eclipse-temurin-21 AS build

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o projeto inteiro (código + arquivos de configuração) para o contêiner
COPY . .

# Compila o projeto e ignora a execução dos testes (-DskipTests)
RUN mvn clean package  shade:shade -DskipTests

# ---- Etapa de Runtime ----
# Use uma imagem de Java Runtime otimizada (somente para execução)
FROM eclipse-temurin:21-jre

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o .jar gerado na etapa de build para a nova imagem
COPY --from=build /app/target/nubank-challenge-1.0-SNAPSHOT.jar app.jar

# Define o comando padrão para executar o jar
CMD ["java", "-jar", "app.jar"]