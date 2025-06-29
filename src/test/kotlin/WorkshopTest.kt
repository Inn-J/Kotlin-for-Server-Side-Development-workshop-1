import org.example.Product
import org.example.celsiusToFahrenheit
import org.example.kilometersToMiles
import org.example.calculateTotalElectronicsPriceOver500
import org.example.quantityofElectronicsPriceOver500
import kotlin.test.Test
import kotlin.test.assertEquals

class WorkshopTest {

    // --- Tests for Workshop #1: Unit Converter ---

    // celsius input: 20.0
    // expected output: 68.0
    @Test
    fun `test celsiusToFahrenheit with positive value`() {
        // Arrange: ตั้งค่า input และผลลัพธ์ที่คาดหวัง
        val celsiusInput = 20.0
        val expectedFahrenheit = 68.0

        // Act: เรียกใช้ฟังก์ชันที่ต้องการทดสอบ
        val actualFahrenheit = celsiusToFahrenheit(celsiusInput)

        // Assert: ตรวจสอบว่าผลลัพธ์ที่ได้ตรงกับที่คาดหวัง
        assertEquals(expectedFahrenheit, actualFahrenheit, 0.001, "20°C should be 68°F")
    }

    // celsius input: 0.0
    // expected output: 32.0
    @Test
    fun `test celsiusToFahrenheit with zero`() {
        val zerocelsius = 0.0
        val expectedFah =32.0
        val actualFah = celsiusToFahrenheit(zerocelsius)
        assertEquals(expectedFah, actualFah, 0.001, "0°C should be 32°F")

    }

    // celsius input: -10.0
    // expected output: 14.0
    @Test
    fun `test celsiusToFahrenheit with negative value`() {
        val negcelsius = -10.0
        val expectedFahren = 14.0
        val actualFahren = celsiusToFahrenheit(negcelsius)
        assertEquals(expectedFahren, actualFahren, 0.001, "-10°C should be -14°F")

    }

    // test for kilometersToMiles function
    // kilometers input: 1.0
    // expected output: 0.621371
    @Test
    fun `test kilometersToMiles with one kilometer`() {
        val kilometers= 1.0
        val expectMile = 0.621371
        val actualMile = kilometersToMiles(kilometers)
        assertEquals(expectMile, actualMile, 0.001, "1 km should be 0.61371 miles")

    }

    // --- Tests for Workshop #1: Unit Converter End ---

    // --- Tests for Workshop #2: Data Analysis Pipeline ---
    // ทำการแก้ไขไฟล์ Workshop2.kt ให้มีฟังก์ชันที่ต้องการทดสอบ
    // เช่น ฟังก์ชันที่คำนวณผลรวมราคาสินค้า Electronics ที่ราคา > 500 บาท
    // ในที่นี้จะสมมุติว่ามีฟังก์ชันชื่อ calculateTotalElectronicsPriceOver500 ที่รับ List<Product> และคืนค่า Double
    // จงเขียน test cases สำหรับฟังก์ชันนี้ โดยตรวจสอบผลรวมราคาสินค้า Electronics ที่ราคา > 500 บาท
    // 🚨
    @Test
    fun `test calculateTotalElectronicsPriceOver500`() {
        //Arrange: ตั้งค่า input และผลลัพธ์ที่คาดหวัง
        val product = listOf(Product("Laptop", 35000.0, category = "Electronics"),
            Product("Smartphone",25000.0,"Electronics"), Product("T-shirt",450.0,"Apparel"),
            Product("Monitor",7500.0, "Electronics"), Product("Keyboard",499.0,"Electronics"),
            Product("Jeans", 1200.0, "Apparel"), Product("Headphones", 1800.0, "Electronics") )
        val expectSum = 69300.0

        // Act: เรียกใช้ฟังก์ชันที่ต้องการทดสอบ
        val actualSum = calculateTotalElectronicsPriceOver500(product)

        // Assert: ตรวจสอบว่าผลลัพธ์ที่ได้ตรงกับที่คาดหวัง
        assertEquals(expectSum, actualSum, 0.001, "It should be 69300.0 ")

    }

    // จงเขียน test cases เช็คจำนวนสินค้าที่อยู่ในหมวด 'Electronics' และมีราคามากกว่า 500 บาท
    // 🚨
    @Test
    fun `test quantityofElectronicsPriceOver500`() {
        //Arrange: ตั้งค่า input และผลลัพธ์ที่คาดหวัง
        val product1 = listOf(Product("Laptop", 35000.0, category = "Electronics"),
            Product("Smartphone",25000.0,"Electronics"), Product("T-shirt",450.0,"Apparel"),
            Product("Monitor",7500.0, "Electronics"), Product("Keyboard",499.0,"Electronics"),
            Product("Jeans", 1200.0, "Apparel"), Product("Headphones", 1800.0, "Electronics") )
        val expectQuantity = 4.0

        // Act: เรียกใช้ฟังก์ชันที่ต้องการทดสอบ
        val actualQuantity = quantityofElectronicsPriceOver500(product1).toDouble()

        // Assert: ตรวจสอบว่าผลลัพธ์ที่ได้ตรงกับที่คาดหวัง
        assertEquals(expectQuantity, actualQuantity, 0.001, "It should be 4")

    }


    // --- Tests for Workshop #2: Data Analysis Pipeline End ---
}