# DemoBlaze E-commerce Testing Framework

---

## 🎯 Project Overview
This project presents a comprehensive testing framework for the DemoBlaze e-commerce application. It highlights enterprise-grade best practices and implements the Page Object Model (POM) design pattern for maintainability and scalability.

---

## ✨ Features
- Robust retry mechanisms for flaky tests  
- Multiple reporting formats: Allure, TestNG, ExtentReports  
- Smart wait strategies for stable test execution  
- Support for custom browser and URL configurations  
- Modular and extensible test suites  

---

## 🛠️ Tech Stack

| Technology       | Version  | Purpose                                  |
|------------------|----------|------------------------------------------|
| Java             | 11       | Programming language                      |
| Selenium         | 4.15.0   | Browser automation framework              |
| TestNG           | Latest   | Test orchestration and assertions         |
| Maven            | Latest   | Build and dependency management           |
| Allure           | Latest   | Advanced test reporting                    |
| WebDriverManager | Latest   | Automatic driver management                |

---

## 🏛️ Project Architecture

The framework is structured into layered modules:
- **Test Layer:** Contains test scripts and suites  
- **Actions Layer:** Business logic and reusable actions  
- **Locators Layer:** UI element locators  
- **Utils Layer:** Utility helpers like wait strategies, retries, logging

## 🎮 Test Scenarios

### 🔐 Login Tests
- Registration, validation, login, logout flows

### 🛒 Cart Tests
- Adding/removing products, price calculations, cart persistence

### 📦 Product Tests
- Product search, filtering, navigation

### 💳 Checkout Tests
- End-to-end checkout process, validations, order confirmation

