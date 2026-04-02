# Profitability Calculation Service

![Java](https://img.shields.io/badge/Java-21-orange?style=flat&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-brightgreen?style=flat&logo=springboot)
![Maven](https://img.shields.io/badge/Build-Maven-blue?style=flat&logo=apache-maven)

Микросервис на базе Spring Boot для расчета доходности инвестиционных активов. Сервис позволяет анализировать прибыльность портфеля, основываясь на данных о покупке и актуальных котировках.

## 🛠 Технологический стек

* **Language:** Java 21
* **Framework:** Spring Boot 4 (Web, Security, DevTools)
* **Build Tool:** Maven
* **Libraries:** Lombok (для чистоты кода), Jackson (для JSON)

## 🚀 Функциональные возможности

* Расчет абсолютной прибыли (Total Profit) по активам.
* Расчет процентной доходности (Percentage Increase).
* Обработка коллекции активов в одном запросе.
* Валидация входных данных о транзакциях.

## 📦 Установка и запуск

### Требования
* JDK 21
* Maven 3.8+

### Шаги по запуску
1. Клонируйте репозиторий:
   ```bash
   git clone [https://github.com/inducechu/profitability-calculation-service.git](https://github.com/inducechu/profitability-calculation-service.git)