package starships.data


import kotlinx.serialization.Serializable
import starships.Player
import starships.factorys.AsteroidSpawn
import starships.factorys.PickUpSpawn
import starships.spaceItems.Asteroid
import starships.spaceItems.Laser
import starships.spaceItems.PickUp


data class GameData(val players: List<Player>, val asteroidSpawns: List<AsteroidSpawn>, val pickUpSpawn: PickUpSpawn,
                    val asteroids: ArrayList<Asteroid>, val laser: ArrayList<Laser>, val pickups: ArrayList<PickUp>)


