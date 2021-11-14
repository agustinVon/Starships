package starships

class SpawnHandler(val space:Space, private val framesBetweenAsteroids: Int, private val framesBetweenPickups: Int){
    private var frameFromLastAsteroid = 0
    private var frameFromLastPickup = 0

    fun passFrame(currentFrames: Int){
        if(currentFrames - frameFromLastAsteroid >= framesBetweenAsteroids){
            space.createAsteroid()
            frameFromLastAsteroid = currentFrames
        }
        if(currentFrames - frameFromLastPickup >= framesBetweenPickups){
            space.createPickups()
            frameFromLastPickup = currentFrames
        }
    }
}