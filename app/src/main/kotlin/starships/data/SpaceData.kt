package starships.data

import starships.factorys.AsteroidSpawn
import starships.factorys.PickUpSpawn
import starships.spaceItems.Asteroid
import starships.spaceItems.Laser
import starships.spaceItems.PickUp

data class SpaceData(val asteroidSpawns: List<AsteroidSpawn>, val pickUpSpawn: PickUpSpawn,
                     val asteroids: ArrayList<Asteroid>, val laser: ArrayList<Laser>, val pickups: ArrayList<PickUp>)
