# CSD-SCRAPER

CSD-SCRAPER is a Java-based application for efficient web data scraping and processing. It automates browser actions, collects data from social media or web sources, and supports keyword-based filtering and emoji handling. The application is configurable and uses external libraries for browser automation, HTML parsing, and database connectivity.

---

## Project Overview

- **Automated scraping** using Selenium WebDriver and ChromeDriver.
- **HTML parsing** with Jsoup.
- **Keyword-based filtering** using `csd_newscorner2_keyword.txt`.
- **Emoji handling** for robust text processing.
- **Configurable** via `csd_config.properties`.
- **Supports MySQL** for data storage (if configured).

---

## Prerequisites

- Java 8 or higher must be installed.
- All required JAR libraries must be placed inside the `jar_lib/` directory.
- ChromeDriver and a compatible version of Chrome (provided in `jar_lib/135driver/` and `GoogleChromePortable64/`).

---

## Configuration

- **`csd_config.properties`**: Main configuration file for database settings, scraping options, and other parameters. Edit this file to customize the scraper's behavior.
- **`csd_newscorner2_keyword.txt`**: List of keywords for filtering or targeting specific content during scraping. Add one keyword per line.

---

## Building and Packaging: How to Compile and Generate JARs

### 1. After Making Code Changes: How to Compile and Generate the JAR

#### **A. Using IntelliJ IDEA**
- Go to `Build > Build Project` (or press `Ctrl+F9`) to compile your code and check for errors.
- To generate the JAR (including the fat JAR), use the Maven tool window:
  1. Open `View > Tool Windows > Maven`.
  2. Under `Lifecycle`, double-click `package`.
  3. This will recompile your code and create a new JAR in the `target/` directory.

#### **B. Using the Command Line**
- Open a terminal in your project directory.
- Run:
  ```sh
  mvn clean package
  ```
- This will compile your code and generate the JAR(s) in the `target/` directory.

---

### 2. How to Generate the Regular JAR

#### **A. Using IntelliJ IDEA**
- In the Maven tool window, double-click `package` under `Lifecycle`.
- The regular JAR will be created in `target/` (e.g., `target/CSD-SCRAPER-1.0-SNAPSHOT.jar`).

#### **B. Using the Command Line**
- Run:
  ```sh
  mvn clean package
  ```
- The regular JAR will be in `target/` (e.g., `target/CSD-SCRAPER-1.0-SNAPSHOT.jar`).

---

### 3. How to Generate the Fat JAR (All Dependencies Included)

#### **A. Using IntelliJ IDEA**
- In the Maven tool window, double-click `package` under `Lifecycle`.
- The fat JAR (with all dependencies) will be created as `target/CSD-SCRAPER.jar`.

#### **B. Using the Command Line**
- Run:
  ```sh
  mvn clean package
  ```
- The fat JAR will be in `target/CSD-SCRAPER.jar`.

---

**Note:**
- The fat JAR is the one you should use for running or distributing your application, as it contains all dependencies.
- To run the fat JAR:
  ```sh
  "C:\Program Files\Java\jdk-13.0.2\bin\java.exe" -Dfile.encoding=UTF-8 -jar target/CSD-SCRAPER.jar
  ```

---

## Running the Application

### **On Windows:**
```sh
java -cp "CSD-SCRAPER.jar;jar_lib/*" csd_newscorner2_auto.main
```
Or use the provided batch file:
```sh
CSD-SCRAPER.bat
```

### **On Linux/Mac:**
Use `:` instead of `;` when specifying the classpath:
```sh
java -cp "CSD-SCRAPER.jar:jar_lib/*" csd_newscorner2_auto.main
```

---

## ChromeDriver & Chrome Setup

- `jar_lib/135driver/chromedriver.exe`: ChromeDriver binary for Selenium automation.
- `GoogleChromePortable64/`: Portable Chrome browser for consistent scraping environment.
- Ensure the ChromeDriver version matches the Chrome version in `GoogleChromePortable64/`.

---

## Troubleshooting

- **Driver/Browser Issues:** Ensure ChromeDriver and Chrome versions are compatible.
- **Missing Dependencies:** All required JARs must be in `jar_lib/`.
- **Java Version:** Use Java 8 or higher.
- **Database Issues:** Check `csd_config.properties` for correct MySQL settings.
- **Recompilation:** If you encounter errors, recompile the project before building the JAR.

---

## Documentation

- For more details, see `CSD-SCRAPER_README.docx` in the project root.

---

