package starships.factorys

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import starships.spaceItems.Laser
import starships.spaceItems.Starship

@Polymorphic
interface Gun {
    fun shoot(starship: Starship): List<Laser>
}