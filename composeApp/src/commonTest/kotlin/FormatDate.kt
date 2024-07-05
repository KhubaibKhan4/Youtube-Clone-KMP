import org.company.app.utils.getFormattedDate
import kotlin.test.Test
import kotlin.test.assertEquals


class FormatDate {

    @Test
    fun testDateFormat(){
        val date = "2024-07-04T23:00:09Z"
        assertEquals("11 hours ago", getFormattedDate(date))
    }
}