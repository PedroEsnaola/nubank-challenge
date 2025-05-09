# Nubank Code Challenge

Aplicação para calculo de ganho de capital em compra e venda de ações.

# Arquitetura e Design Patterns Aplicados

No Projeto, foi utilizado DDD, visando garantir a correta separacão de responsabilidade entre as camadas
da aplicação. Com isso, o projeto está separado nas seguintes camadas:

## Presentation

Essa camada tem como objetivo, permitir entrada e saida de dados de forma que o domínio, a regra de negócio, não esteja
acoplada a apenas um tipo de entrada
Para isso, essa camada é responsável pelo processo de manipulação e adaptação da entrada e saida para o domínio,
seja isso serialização/deserialização ou emissão e consumo de dados por uma fila

## Application

A camada de Application tem como responsabilidade manter toda a regra de negócio da aplicação, interagindo com a camada
de model e sendo acessada pela camada de presentation, e tambem acessada internamente pela propria camada de application

## Model

Essa camada tem a responsabilidade de agrupar todo o modelo do domínio, assim, mantendo todas as classes que representam
os dados trafegados para tao bem como de dentro da camada de application

# Design Patterns

Com o objetivo de garantir extensibilidade, testabilidade e um codigo limpo e claro, foi utilizado no calculo das
operacões financeiras o padrão factory, visando a correta separação da responsabilidade entre operações de compra e de
venda e permitindo, com facilidade a adesão de novas modalidades de operações
Também, foram aplicados ao longo de todo o projeto, conceitos de solid e orientação a objetos, como polimorfismo para as
implementações de cada interface de processamento de operações, como também uso de principios como Open-Closed e o
Principio de Substituição de Liskov

## Tecnologias Adotada

A linguagem escolhida para implementar a funcionalidade foi o Java, sendo ele uma linguagem de ampla utilização, robusta
e com um rico ecossistema, permitindo assim o desenvolvimento de uma aplicação robusta e escalável
Para gerenciamento de dependências, foi escolhido o maven, sendo ele também o responsável pelo build da aplicacao

## Executando a aplicação

A fim de facilitar a execução, a aplicação pode ser **Contenerizada**, para isso é necessário possuir
o [Docker](https://www.docker.com/) instalado, com ele configurado, basta seguir o passo a passo a seguir para realizar
o build

- Abra o terminal na pasta do projeto
- Já na pasta raiz, execute o seguinte comando
  ```bash
  docker build -t nubank-challenge:latest .
  ```
- Com isso, será gerado a **Imagem** do container a ser rodado, após isso, execute o comando
  ```bash
  docker run -i -t nubank-challenge 
  ```
- Esse comando, irá efetivamente inicializar o container a partir da imagem, os argumentos -i e -t indicam que após
  iniciado, um terminal interativo ficará disponivel para permitir o uso da ferramenta

## Executando os testes

Com o objetivo de garantir o correto funcionamento da aplicação, foram inseridos testes unitarios e integrados
utilizando Junit5
Para executar os testes é necessário possuir o [Maven](https://maven.apache.org/) instalado

- Abra o terminal na pasta do projeto
- Execute o comando
  ```bash
  mvn test
  ```
- Com isso os testes serão executados e validados