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

## 🛠️ Coming Soon

- Live trade simulation with balance tracking
- Reinforcement learning based signal adaptation
- Web dashboard for strategy monitoring

---

> Built with love for data-driven trading & Java engineering ♥️
