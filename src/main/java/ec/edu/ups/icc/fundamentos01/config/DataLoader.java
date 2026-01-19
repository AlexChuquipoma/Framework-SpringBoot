package ec.edu.ups.icc.fundamentos01.config;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public DataLoader(
            ProductRepository productRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Solo cargar datos si la base de datos está vacía
        if (productRepository.count() == 0) {
            System.out.println("🔄 Cargando datos iniciales...");

            // ========== CREAR USUARIOS ==========
            UserEntity user1 = new UserEntity();
            user1.setName("Juan Pérez");
            user1.setEmail("juan@email.com");
            user1.setPassword("password123");
            userRepository.save(user1);

            UserEntity user2 = new UserEntity();
            user2.setName("María García");
            user2.setEmail("maria@email.com");
            user2.setPassword("password456");
            userRepository.save(user2);

            // ========== CREAR CATEGORÍAS ==========
            CategoryEntity catElectronicos = new CategoryEntity();
            catElectronicos.setName("Electrónicos");
            catElectronicos.setDescription("Dispositivos electrónicos y tecnología");
            categoryRepository.save(catElectronicos);

            CategoryEntity catGaming = new CategoryEntity();
            catGaming.setName("Gaming");
            catGaming.setDescription("Productos para gamers");
            categoryRepository.save(catGaming);

            CategoryEntity catOficina = new CategoryEntity();
            catOficina.setName("Oficina");
            catOficina.setDescription("Equipos y accesorios de oficina");
            categoryRepository.save(catOficina);

            CategoryEntity catMuebles = new CategoryEntity();
            catMuebles.setName("Muebles");
            catMuebles.setDescription("Mobiliario para el hogar y oficina");
            categoryRepository.save(catMuebles);

            // ========== CREAR PRODUCTOS CON RELACIONES N:N ==========

            // Producto 1: Laptop Gamer (Electrónicos + Gaming + Oficina)
            ProductEntity producto1 = new ProductEntity();
            producto1.setName("Laptop Gamer");
            producto1.setDescription("Potente laptop con RTX 4060");
            producto1.setPrice(1200.5);
            producto1.setOwner(user1);
            Set<CategoryEntity> cats1 = new HashSet<>();
            cats1.add(catElectronicos);
            cats1.add(catGaming);
            cats1.add(catOficina);
            producto1.setCategories(cats1);
            productRepository.save(producto1);

            // Producto 2: Mouse (Electrónicos + Gaming + Oficina)
            ProductEntity producto2 = new ProductEntity();
            producto2.setName("Mouse Logitech G502");
            producto2.setDescription("Sensor Hero 25k, RGB, Pesas ajustables");
            producto2.setPrice(45.5);
            producto2.setOwner(user1);
            Set<CategoryEntity> cats2 = new HashSet<>();
            cats2.add(catElectronicos);
            cats2.add(catGaming);
            cats2.add(catOficina);
            producto2.setCategories(cats2);
            productRepository.save(producto2);

            // Producto 3: Teclado (Electrónicos + Gaming + Oficina)
            ProductEntity producto3 = new ProductEntity();
            producto3.setName("Teclado Redragon");
            producto3.setDescription("Mecánico, switches azules, luces RGB");
            producto3.setPrice(35.0);
            producto3.setOwner(user2);
            Set<CategoryEntity> cats3 = new HashSet<>();
            cats3.add(catElectronicos);
            cats3.add(catGaming);
            cats3.add(catOficina);
            producto3.setCategories(cats3);
            productRepository.save(producto3);

            // Producto 4: Monitor (Electrónicos + Gaming)
            ProductEntity producto4 = new ProductEntity();
            producto4.setName("Monitor Samsung 27\"");
            producto4.setDescription("Resolución 4K, Panel IPS, 144Hz");
            producto4.setPrice(320.0);
            producto4.setOwner(user2);
            Set<CategoryEntity> cats4 = new HashSet<>();
            cats4.add(catElectronicos);
            cats4.add(catGaming);
            producto4.setCategories(cats4);
            productRepository.save(producto4);

            // Producto 5: Silla (Muebles + Gaming + Oficina)
            ProductEntity producto5 = new ProductEntity();
            producto5.setName("Silla Corsair T3");
            producto5.setDescription("Ergonómica, reclinable 180 grados");
            producto5.setPrice(280.0);
            producto5.setOwner(user1);
            Set<CategoryEntity> cats5 = new HashSet<>();
            cats5.add(catMuebles);
            cats5.add(catGaming);
            cats5.add(catOficina);
            producto5.setCategories(cats5);
            productRepository.save(producto5);

            System.out.println("✅ Datos iniciales cargados exitosamente!");
            System.out.println("   - 2 usuarios creados");
            System.out.println("   - 4 categorías creadas");
            System.out.println("   - 5 productos con relaciones N:N creados");
        } else {
            System.out.println("ℹ️  Ya existen datos en la base de datos. No se cargan datos iniciales.");
        }
    }
}
