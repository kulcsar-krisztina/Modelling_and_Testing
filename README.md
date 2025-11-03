# 🎫 BudapestGO Ticket System - Model-Based Testing

## 🎯 Overview

This project demonstrates **Model-Based Testing (MBT)** applied to a real-world ticket purchase system. The BudapestGO mobile application allows users to buy public transport tickets using various payment methods.

### **What is Model-Based Testing?**

Model-Based Testing is an automated testing approach where:
1. **System behavior** is modeled as a Finite State Machine (FSM)
2. **Test cases** are automatically generated from the model
3. **Different algorithms** explore the model to achieve various coverage goals

### **Why MBT?**

✅ **Automated test generation** - No manual test case writing  
✅ **Complete coverage** - Systematic exploration of all states and transitions  
✅ **Early defect detection** - Find bugs before implementation  
✅ **Regression testing** - Easily re-run tests after changes  
✅ **Documentation** - Model serves as living documentation  

---

## ✨ Features

### **Ticket Types**
| Type | Validity | Price (HUF) | Description |
|------|----------|-------------|-------------|
| 🎫 Single | 80 minutes | 350 | Valid for a single journey |
| 📅 Day Pass | 24 hours | 1,650 | Unlimited travel for one day |
| 📆 Weekly Pass | 7 days | 4,950 | Unlimited travel for one week |
| 📊 Monthly Pass | 30 days | 9,500 | Unlimited travel for one month |

### **Payment Methods**
- 💳 **Credit/Debit Card** (Visa, Mastercard)
- 📱 **Google Pay** (Android devices)
- 🍎 **Apple Pay** (iOS devices)

### **System Capabilities**
- ✅ Ticket selection and purchase
- ✅ Multiple payment method support
- ✅ QR code generation
- ✅ Ticket validation and activation
- ✅ Automatic expiration handling
- ✅ Payment retry mechanism (max 3 attempts)
- ✅ Purchase cancellation
