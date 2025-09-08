import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.company.app.core.common.getFormattedDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


class FormatDate {

    @Test
    fun testUnknowDateFormat(){
        val date = Clock.System.now().toEpochMilliseconds().toString()
        assertEquals("Unknown date", getFormattedDate(date))
    }

    @Test
    fun testWrongDateFormat() {
        val date = "2024-07-04T23:00:09Z"
        assertNotEquals("12 hours ago", getFormattedDate(date))
    }

    @Test
    fun testRightDateFormat() {
        val inputDate = "2024-07-04T23:00:09Z"
        val parsedIntent = Instant.parse(inputDate)

        val now = Clock.System.now()
        val duration = now.epochSeconds - parsedIntent.epochSeconds
        val expectedDays = duration / (60 * 60 * 24)
        val expected = "$expectedDays days ago"
        val actual = getFormattedDate(inputDate)


        assertEquals(expected, actual)
    }
}