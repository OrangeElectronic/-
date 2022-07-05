package app.com.tsport;
import android.app.Application
import com.jianzhi.glitter.GlitterActivity

class MyApp :Application(){
    override fun onCreate() {
        super.onCreate()
        GlitterActivity.setUp("file:///android_asset/appData",appName = "appData")
    }
}