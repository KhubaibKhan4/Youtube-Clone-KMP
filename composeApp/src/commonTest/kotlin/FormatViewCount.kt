import androidx.compose.runtime.Composable
import org.company.app.utils.formatViewCount
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatViewCount {
    @Test
    fun testViewCount(){
        val videoViews = "11764486"
        assertEquals("11M", org.company.app.presentation.ui.screens.detail.formatViewCount(videoViews))
    }
}