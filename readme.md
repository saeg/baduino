# Baduino Eclipse Plugin #
## Bitwise-Algorithm Definition-Use assocIatioN visualizatiOn##
[![Build Status](https://travis-ci.org/saeg/baduino.svg?branch=dev)](https://travis-ci.org/saeg/baduino)
-------------

# O que é?
Baduíno é um Plugin do Eclipse para visualização de cobertura de critério de testes baseado em fluxo de dados intraprocedimental, ou seja, exibe as associações definição-uso cobertas em cada método do projeto.

* Associaçoes definição-uso (DUA) definem uma relação entre a definição de uma váriavel e o seu subsequente uso.

# Por quê?
A análise de cobertura de critérios baseado em fluxo de dados é muito custosa, em parte devido ao alto custo de rastrear as DUAs em tempo de execução. Por isso, dificilmente são usadas na prática. 

Para tornar a análise de cobertura de critérios baseado em fluxo de dados escalável para programas reais, a Baduíno utiliza a estratégia para cálculo de cobertura de fluxo de dados [Bitwise-Algorithm](http://www.sciencedirect.com/science/article/pii/S0020019013000537).

# Como?
A cobertura é gerada executando os testes com um JUnit Runner. Esse Runner roda os testes com a ferramenta [ba-dua](https://github.com/saeg/ba-dua) como agente.

A Ba-dua (Bitwise Algorithm-powered Definition-Use Association) é a ferramenta que implementa o cálculo de cobertura de DUAs.

O resultado da cobertura obtida é apresentado no Eclipse pela Baduino.
