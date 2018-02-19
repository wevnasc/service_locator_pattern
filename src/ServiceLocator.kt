import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.jvmName

object ServiceLocator {

    var serviceInstances = HashMap<String, Any>()
    var serviceImplementationsMapping = HashMap<String, KClass<*>>()
    var serviceInstance = Any()

    interface Service {
        //don't body
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(clazz: KClass<*>): T {
        return getService(clazz.jvmName) as T
    }

    @Synchronized fun  set(interfaceClass: KClass<*>, implementationClass: KClass<*>) {
        serviceImplementationsMapping[interfaceClass.jvmName] = implementationClass
    }

    @Synchronized private fun getService(name: String): Any {
        val serviceCacheInstance = serviceInstances[name]
        if (serviceCacheInstance != null) {
            return serviceCacheInstance
        }
        try {
            val clazz: KClass<*>? = if (serviceImplementationsMapping.containsKey(name)) {
                serviceImplementationsMapping[name]
            } else {
                Class.forName(name).kotlin
            }

            try {
                serviceInstance = clazz!!.createInstance()
            } catch (e: NoSuchMethodException) {
                throw IllegalArgumentException("Service without default constructor: " + name, e)
            }

            if(serviceInstance !is ServiceLocator.Service) {
                throw IllegalArgumentException("Requested service must implement IService interface")
            }

            serviceInstances[name] = serviceInstance
            return serviceInstance

        } catch (e: ClassNotFoundException) {
            throw IllegalArgumentException("Requested service class was not found: " + name, e)
        } catch (e: Exception) {
            throw IllegalArgumentException("Cannot initialize requested service: " + name, e)
        }
    }
}