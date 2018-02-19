fun main(args: Array<String>) {

    ServiceLocator.set(ProductInterface::class, Product::class)
    ServiceLocator.set(CustomerInterface::class, Customer::class)

    println((ServiceLocator.get(CustomerInterface::class) as CustomerInterface).getText())
    println((ServiceLocator.get(ProductInterface::class) as ProductInterface).getText())
    println((ServiceLocator.get(CustomerInterface::class) as CustomerInterface).getText())
    println((ServiceLocator.get(ProductInterface::class) as ProductInterface).getText())

}