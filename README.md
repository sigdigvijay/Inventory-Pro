# 📦 **InventoryPro – Warehouse & Order Management System**

InventoryPro is a complete warehouse management platform designed to handle **product catalogs**,  
**stock levels**, **supplier orders**, **sales**, and **analytical reports**.

---

## 🖥️ **Screen 1 – Product Catalog**

### ⭐ **Product Management**
- **Product code** (unique, auto or manual)
- **Name**
- **Detailed description**
- **Category / Subcategory**
- **Selling price**  
- **Purchase price**
- **Product image** (path or URL)

### 🗂️ **Category Management**
- Create **categories** & **subcategories**
- Assign **colors** or **icons**
- View products by category

### 🔎 **Search, Filter & Sort**
- Search by **product code**
- Partial search by **name**
- Filters:
  - **Category**
  - **Price range**
- Sort by:
  - **Name**
  - **Price**
  - **Stock**
  - **Date added**

### 🖼️ **Display Modes**
- **Grid view** with images  
- **Detailed list view**  
- Automatic highlight for **low-stock products**

---

## 📊 **Screen 2 – Inventory & Movements**

### 📦 **Stock Overview**
- Current **stock quantity**
- **Minimum** and **maximum** thresholds
- **Total stock value** (price × qty)
- Filter products **below minimum stock**

### 🔁 **Register Movements**
**Movement types:**
- **Incoming (+):** purchases, customer returns  
- **Outgoing (−):** sales, returns to supplier, damaged goods  

Details:
- Select product  
- Quantity  
- Reason  
- Date (auto or manual)  
- Notes (optional)

### 📜 **Movement History**
- Complete movement list  
- Filters: type, product, date range, reason  
- Movement detail view

### ⚠️ **Automatic Alerts (Bonus)**
- Visual alert for **low stock**
- Red badge for critical items
- Auto-generated **reorder list**

---

## 🧾 **Screen 3 – Supplier Orders**

### 🧑‍💼 **Supplier Registry**
- Company name / Supplier name  
- VAT number  
- Address  
- Phone, Email  
- Notes  
- Linked products

### 📝 **Create Purchase Orders**
- Select supplier  
- Add products:
  - Product  
  - Quantity  
  - Unit purchase price  
- Order date  
- Expected delivery date  
- **Automatic total calculation**

### 🔄 **Order Status Management**
**Statuses:**

- Manual status change  
- View orders by status  

### 📥 **Goods Reception**
When order becomes **Received**:
- Automatic **stock increase**
- Automatic **movement logs**
- Support **partial deliveries**

### 🗄️ **Order History**
- Complete order list  
- Filters: supplier, status, date  
- Search by order number  
- Detailed view

---

## 💰 **Screen 4 – Sales & Statistics**

### 🧾 **Sales Registration**
- Select sold products  
- Quantity  
- Editable **selling price**  
- Sale date  
- Customer (optional)  
- Automatic **stock reduction**

### 📈 **Dashboard & Analytics**
- **Top-selling** products
- **Least-selling** products  
- **Highest-margin** items  
- **Total revenue** (daily / monthly / yearly)
- **Current total inventory value**

### 💹 **Margin Analysis**
- Margin per product:  
  `margin = (selling price - purchase price) × quantity sold`
- Margin per category  
- Identify **loss-making** products

### 🕒 **Time-Based Reports**
- Sales per **day** / **week** / **month**
- Sales **trend over time**
- **Period comparison** (e.g., month vs previous month)

### 📊 **Charts (Bonus)**
- **Bar chart:** sales by category  
- **Line chart:** monthly revenue trend  
- **Pie chart:** inventory value distribution  

### 📤 **Data Export**
- Export inventory to **CSV / Excel**
- Export **sales reports**
- Optional **print mode**

---

## 🛠️ **Suggested Tech Stack**
*(Customize based on your tech choices)*

- **Frontend:** JAVA  
- **Backend:** JAVA  
- **Database:** SQLite  
- **Auth:** TBA  
- **Deployment:** TBA

---

## 🚧 **Project Status**
This is an ongoing group project implementing **CRUD logic**,  
**inventory workflows**, **order processing**, and **analytics dashboards**.

---

## 📜 **License**
This project is licensed under the **MIT License**.

