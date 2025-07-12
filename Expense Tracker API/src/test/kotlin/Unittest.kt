import com.example.ReportService
import kotlin.test.Test
import kotlin.test.assertEquals
class Unittest {
    @Test
    fun testGenerateYearlySummary_2024() {
        val summary = ReportService.generateYearlySummary(2024)
        assertEquals(30000.0, summary.totalIncome, "Total income should be 30000.0")
        assertEquals(125.0, summary.totalExpense, "Total expense should be 125.0")
        assertEquals(29875.0, summary.balance, "Balance should be 29875.0")
    }

    @Test
    fun testGenerateYearlyCategoryBreakdown_2024() {
        val categorySummary = ReportService.generateYearlyCategoryBreakdown(2024)

        val summaryMap = categorySummary.byCategory.associateBy { it.category }
        assertEquals(2, summaryMap.size, "Should have 2 expense categories")

        assertEquals(95.0, summaryMap["อาหาร"]?.total, "Total for 'อาหาร' should be 95.0")
        assertEquals(30.0, summaryMap["เดินทาง"]?.total, "Total for 'เดินทาง' should be 30.0")
    }
}