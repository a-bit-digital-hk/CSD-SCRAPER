# CSD-SCRAPER

CSD-SCRAPER is a Java-based application designed for efficient data scraping and processing. It supports dependency management and runs with external libraries.

---

## Prerequisites

Before using CSD-SCRAPER, ensure the following requirements are met:

- Java 8 or higher must be installed.
- Required JAR libraries should be placed inside the `jar_lib/` directory.

---

## Building the JAR File

To compile and package the project into a JAR file, follow these steps:

1. Navigate to the project directory:
   ```sh
   cd C:\CSD-SCRAPER
   ```

2. Run the following command to create the JAR file:
   ```sh
   jar cfm CSD-SCRAPER.jar C:\CSD-SCRAPER\csd_newscorner2_auto\src\META-INF\MANIFEST.MF -C C:\CSD-SCRAPER\out\production\CSD-SCRAPER .
   ```

   **Note:** Ensure that all compiled classes are available in `out/production/CSD-SCRAPER` before running this command.

---

## Running the Application

Once the JAR file is built, you can execute it using the following command:

### **On Windows:**
```sh
java -cp "CSD-SCRAPER.jar;jar_lib/*" csd_newscorner2_auto.main
```

### **On Linux/Mac:**
Use `:` instead of `;` when specifying the classpath:
```sh
java -cp "CSD-SCRAPER.jar:jar_lib/*" csd_newscorner2_auto.main
```

---

## Additional Notes

- Ensure that all compiled classes are present in `out/production/CSD-SCRAPER` before running the application.
- All dependencies must be placed inside the `jar_lib/` directory.
- If you encounter any issues, try recompiling the project before building the JAR file.

---

