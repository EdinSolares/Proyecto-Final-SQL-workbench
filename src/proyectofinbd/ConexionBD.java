package proyectofinbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/proyectofinal";
    private static final String USER = "root";
    private static final String PASSWORD = "BlueDragon123*";

    // Método para establecer la conexión
    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexion exitosa a la base de datos");
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return conexion;
    }

    // Método para insertar un nuevo producto
    public static void insertarProducto(String codigo, String nombre, double precio, int cantidad, String fecha) {
        String query = "INSERT INTO producto (codigoProducto, nombreProducto, precioUnitario, cantidadProducto, fechaVencimiento) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = conectar();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setString(1, codigo);
            pst.setString(2, nombre);
            pst.setDouble(3, precio);
            pst.setInt(4, cantidad);
            pst.setDate(5, java.sql.Date.valueOf(fecha));
            
            pst.executeUpdate();
            System.out.println("Producto insertado correctamente");
        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
        }
    }

    // Método para listar todos los productos
    public static void listarProductos() {
        String query = "SELECT * FROM producto ORDER BY nombreProducto";
        try (Connection con = conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            System.out.println("\n=== LISTADO DE PRODUCTOS ===");
            boolean hayProductos = false;
            
            while (rs.next()) {
                hayProductos = true;
                System.out.println("\nCodigo: " + rs.getString("codigoProducto"));
                System.out.println("Nombre: " + rs.getString("nombreProducto"));
                System.out.println("Precio: $" + rs.getDouble("precioUnitario"));
                System.out.println("Cantidad: " + rs.getInt("cantidadProducto"));
                System.out.println("Fecha de Vencimiento: " + rs.getDate("fechaVencimiento"));
                System.out.println("-----------------------------");
            }
            
            if (!hayProductos) {
                System.out.println("No hay productos registrados.");
            }
            
        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
    }

    // Método para buscar producto por código
    public static void buscarProducto(String codigo) {
        String query = "SELECT * FROM producto WHERE codigoProducto = ?";
        try (Connection con = conectar();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setString(1, codigo);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                System.out.println("\nProducto encontrado:");
                System.out.println("Codigo: " + rs.getString("codigoProducto"));
                System.out.println("Nombre: " + rs.getString("nombreProducto"));
                System.out.println("Precio: $" + rs.getDouble("precioUnitario"));
                System.out.println("Cantidad: " + rs.getInt("cantidadProducto"));
                System.out.println("Fecha de Vencimiento: " + rs.getDate("fechaVencimiento"));
            } else {
                System.out.println("Producto no encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.getMessage());
        }
    }

    // Método para buscar producto por nombre
    public static void buscarProductoPorNombre(String nombre) {
        String query = "SELECT * FROM producto WHERE nombreProducto LIKE ?";
        try (Connection con = conectar();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setString(1, "%" + nombre + "%");
            ResultSet rs = pst.executeQuery();
            boolean encontrado = false;
            
            System.out.println("\n=== Resultados de la busqueda ===");
            while (rs.next()) {
                encontrado = true;
                System.out.println("\nCodigo: " + rs.getString("codigoProducto"));
                System.out.println("Nombre: " + rs.getString("nombreProducto"));
                System.out.println("Precio: $" + rs.getDouble("precioUnitario"));
                System.out.println("Cantidad: " + rs.getInt("cantidadProducto"));
                System.out.println("Fecha de Vencimiento: " + rs.getDate("fechaVencimiento"));
                System.out.println("-----------------------------");
            }
            
            if (!encontrado) {
                System.out.println("No se encontraron productos con ese nombre.");
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.getMessage());
        }
    }

    // Método para actualizar producto
    // Método para actualizar producto
public static void actualizarProducto(String codigoActual, String nuevoCodigo, String nombre, double precio, int cantidad) {
    String query = "UPDATE producto SET codigoProducto = ?, nombreProducto = ?, precioUnitario = ?, cantidadProducto = ? WHERE codigoProducto = ?";
    try (Connection con = conectar();
         PreparedStatement pst = con.prepareStatement(query)) {
        
        pst.setString(1, nuevoCodigo);
        pst.setString(2, nombre);
        pst.setDouble(3, precio);
        pst.setInt(4, cantidad);
        pst.setString(5, codigoActual);
        
        int filasActualizadas = pst.executeUpdate();
        if (filasActualizadas > 0) {
            System.out.println("Producto actualizado correctamente");
        } else {
            System.out.println("No se encontro el producto con el codigo especificado");
        }
    } catch (SQLException e) {
        System.out.println("Error al actualizar producto: " + e.getMessage());
    }
}

    // Método para eliminar producto
    public static void eliminarProducto(String codigo) {
        String query = "DELETE FROM producto WHERE codigoProducto = ?";
        try (Connection con = conectar();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setString(1, codigo);
            
            int filasEliminadas = pst.executeUpdate();
            if (filasEliminadas > 0) {
                System.out.println("Producto eliminado correctamente");
            } else {
                System.out.println("No se encontró el producto con el codigo especificado");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
        }
    }

    // Método para realizar una venta
    public static void realizarVenta(String codigo, int cantidadVenta) {
        Connection con = null;
        try {
            con = conectar();
            con.setAutoCommit(false); // Inicio de la transacción
            
            // Verificar existencia y stock
            String queryStock = "SELECT cantidadProducto, precioUnitario, nombreProducto FROM producto WHERE codigoProducto = ?";
            PreparedStatement pstStock = con.prepareStatement(queryStock);
            pstStock.setString(1, codigo);
            ResultSet rs = pstStock.executeQuery();
            
            if (rs.next()) {
                int stockActual = rs.getInt("cantidadProducto");
                double precio = rs.getDouble("precioUnitario");
                String nombreProducto = rs.getString("nombreProducto");
                
                if (stockActual >= cantidadVenta) {
                    // Actualizar inventario
                    String updateStock = "UPDATE producto SET cantidadProducto = cantidadProducto - ? WHERE codigoProducto = ?";
                    PreparedStatement pstUpdate = con.prepareStatement(updateStock);
                    pstUpdate.setInt(1, cantidadVenta);
                    pstUpdate.setString(2, codigo);
                    pstUpdate.executeUpdate();
                    
                    // Calcular total
                    double total = precio * cantidadVenta;
                    
                    // Imprimir factura
                    System.out.println("\n========= FACTURA =========");
                    System.out.println("Fecha: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    System.out.println("-------------------------");
                    System.out.println("Producto: " + nombreProducto);
                    System.out.println("Codigo: " + codigo);
                    System.out.println("Cantidad: " + cantidadVenta);
                    System.out.println("Precio unitario: $" + precio);
                    System.out.println("-------------------------");
                    System.out.println("Total a pagar: $" + total);
                    System.out.println("=========================");
                    
                    con.commit(); // Confirmar transacción
                    System.out.println("\nVenta realizada con exito.");
                } else {
                    System.out.println("Error: Stock insuficiente.");
                    System.out.println("Stock actual: " + stockActual);
                    System.out.println("Cantidad solicitada: " + cantidadVenta);
                    con.rollback();
                }
            } else {
                System.out.println("Error: Producto no encontrado.");
                con.rollback();
            }
        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                System.out.println("Error al realizar rollback: " + ex.getMessage());
            }
            System.out.println("Error al realizar la venta: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexion: " + e.getMessage());
            }
        }
    }

    // Método para generar reporte de inventario
    public static void generarReporteInventario() {
        String query = "SELECT codigoProducto, nombreProducto, precioUnitario, cantidadProducto, " +
                      "fechaVencimiento FROM producto ORDER BY nombreProducto";
        try (Connection con = conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            System.out.println("\n=============== REPORTE DE INVENTARIO ===============");
            System.out.println("Fecha: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            System.out.println("===================================================");
            System.out.printf("%-12s %-25s %-10s %-10s %-12s%n", 
                            "CODIGO", "NOMBRE", "PRECIO", "STOCK", "VENCIMIENTO");
            System.out.println("---------------------------------------------------");
            
            double valorTotal = 0;
            int totalProductos = 0;
            
            while (rs.next()) {
                String codigo = rs.getString("codigoProducto");
                String nombre = rs.getString("nombreProducto");
                double precio = rs.getDouble("precioUnitario");
                int cantidad = rs.getInt("cantidadProducto");
                Date fechaVenc = rs.getDate("fechaVencimiento");
                
                valorTotal += precio * cantidad;
                totalProductos += cantidad;
                
                System.out.printf("%-12s %-25s $%-9.2f %-10d %-12s%n",
                                codigo, nombre, precio, cantidad,
                                new SimpleDateFormat("yyyy-MM-dd").format(fechaVenc));
            }
            
            System.out.println("===================================================");
            System.out.printf("Total de productos en inventario: %d%n", totalProductos);
            System.out.printf("Valor total del inventario: $%.2f%n", valorTotal);
            System.out.println("===================================================");
            
        } catch (SQLException e) {
            System.out.println("Error al generar reporte: " + e.getMessage());
        }
    }
}