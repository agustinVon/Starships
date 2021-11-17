package starships.factorys

import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.milliseconds
import edu.austral.dissis.starships.file.ImageLoader
import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.image.ImageView
import kotlinx.serialization.Contextual
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import starships.spaceItems.Laser
import starships.spaceItems.Starship

@Serializable
class FastGun(val speed: Double): Gun() {
    @Contextual
    @Transient
    private val laserGun = LaserGun(speed, 100)
    override fun shoot(starship: Starship): List<Laser> {
        return laserGun.shoot(starship)
    }

}