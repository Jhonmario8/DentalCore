package com.lc.dentalcore;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Deshabilitado a propósito: levantar el contexto completo abre una conexión real usando
 * LC_DB_URL / LC_DB_USERNAME / LC_DB_PASSWORD. Si esas variables apuntan a la base del
 * consultorio, un simple {@code ./gradlew build} se conectaría a datos reales de pacientes.
 * Los tests del proyecto son unitarios (Mockito) y no necesitan Spring ni base de datos.
 */
@Disabled("Requiere una base de datos real; los tests del proyecto son unitarios")
@SpringBootTest
class DentalCoreApplicationTests {

    @Test
    void contextLoads() {
    }

}
