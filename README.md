#  crypto-signal-ai

**AI-powered crypto signal bot** using TA4J for technical indicators, Smile for ML-based predictions, and Spring Boot with MongoDB for full-stack automation.

##  What it does

This bot continuously monitors selected cryptocurrencies and:

- Fetches live market data (via CoinGecko API)
- Calculates key indicators: **RSI**, **MACD**, **Bollinger Bands** using **ta4j**
- Generates **Buy/Sell signals** with:
    - Rule-based logic
    - Machine learning models (Random Forest / MLP via **Smile**)
- Sends alerts via **Telegram** and **Email**
- Logs all actions into **MongoDB**
- Supports **simulated trading** and **backtesting**
- Generates labeled **CSV datasets** for model training

---

## 🚀 Tech Stack

- **Java 17**
- **Spring Boot**
- **TA4J** – Technical analysis library
- **Smile** – Machine learning library
- **MongoDB** – Sinyal/işlem geçmişi
- **Telegram Bot API**, **JavaMail**
- **CoinGecko API** – Real-time price data

---

## ⚙️ Features

- 🧠 AI-based signal prediction
- ⏱️ 5-minute interval scanning with Spring Scheduler
- 🧪 TradeSimulator for historical backtests
- 🗂️ CSV exporter for ML training data
- 🔔 Multi-channel notifications (email, Telegram)
- 💾 MongoDB signal & trade history storage

---

## ⚙️ Configuration (`application.properties`)

To run the project successfully, you need to configure the following properties inside your `src/main/resources/application.properties` file:

---

### 📧 Email Settings (via Gmail SMTP)

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_gmail@gmail.com
spring.mail.password=your_gmail_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

alert.mail.to=receiver@gmail.com
```
spring.mail.username: The sender Gmail address.

spring.mail.password: App password generated via Google (not your Gmail login password).

alert.mail.to: Receiver email address for alerts.
️ Important: You must enable 2FA in your Google account and generate an App Password to use SMTP.

### 📲 Telegram Bot Settings
``` properties
telegram.bot.token=123456789:ABCDEF1234567890abcdef
telegram.chat.id=-123456789
```
telegram.bot.token: Token from @BotFather after creating a new bot.

telegram.chat.id: Chat ID of the Telegram user or group where alerts will be sent.

For group chat IDs, make sure the bot is added to the group and use a negative sign (e.g., -123456789).

### 🍃 MongoDB Settings

```properties
#spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=cpa
spring.jpa.properties.hibernate.ogm.datastore.create_database=true
spring.data.mongodb.auto-index-creation=true

```
MongoDB is used to store signal history and trades.

Default configuration assumes MongoDB is running locally on port 27017.

Database name: cpa

## 🛠️ Coming Soon

- Live trade simulation with balance tracking
- Reinforcement learning based signal adaptation
- Web dashboard for strategy monitoring

---

> Built with love for data-driven trading & Java engineering ♥️
