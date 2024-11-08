package proyectofinbd;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Proyectofinbd {
    private static boolean sesionIniciada = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Proceso de login
        if (!iniciarSesion(scanner)) {
            System.out.println("Programa terminado.");
            return;
        }

        // Menu principal
        mostrarMenuPrincipal(scanner);
        
        scanner.close();
    }
    
    private static boolean iniciarSesion(Scanner scanner) {
        int intentos = 3;
        
        while (intentos > 0) {
            System.out.println("\n=== BIENVENIDO AL SISTEMA DE INVENTARIO ===");
            System.out.println("Ingrese sus datos para acceder: " + intentos);
            System.out.print("Usuario: ");
            String usuario = scanner.nextLine();
            System.out.print("Pasw: ");
            String password = scanner.nextLine();

            if (Login.validarUsuario(usuario, password)) {
                System.out.println("Inicio de sesion exitoso!");
                sesionIniciada = true;
                return true;
            } else {
                intentos--;
                if (intentos > 0) {
                    System.out.println("Credenciales incorrectas. Por favor, intente nuevamente.");
                }
            }
        }
        
        System.out.println("Numero maximo de intentos alcanzado.");
        return false;
    }
    
    private static void mostrarMenuPrincipal(Scanner scanner) {
        int opcion = 0;
        do {
            System.out.println("\n***************************");
            System.out.println("       Menu Principal       ");
            System.out.println("***************************");
            System.out.println("1.....Ingresar producto");
            System.out.println("2.....Mostrar productos");
            System.out.println("3.....Buscar producto por codigo");
            System.out.println("4.....Buscar producto por nombre");
            System.out.println("5.....Modificar producto");
            System.out.println("6.....Eliminar producto");
            System.out.println("7.....Realizar venta");
            System.out.println("8.....Generar reporte de inventario");
            System.out.println("9.....Cerrar sesion");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer

                switch (opcion) {
                    case 1:
                        ingresarProducto(scanner);
                        break;
                    case 2:
                        ConexionBD.listarProductos();
                        break;
                    case 3:
                        buscarProductoPorCodigo(scanner);
                        break;
                    case 4:
                        buscarProductoPorNombre(scanner);
                        break;
                    case 5:
                        modificarProducto(scanner);
                        break;
                    case 6:
                        eliminarProducto(scanner);
                        break;
                    case 7:
                        realizarVenta(scanner);
                        break;
                    case 8:
                        ConexionBD.generarReporteInventario();
                        break;
                    case 9:
                        System.out.println("Cerrando sesion...");
                        sesionIniciada = false;
                        break;
                    default:
                        System.out.println("Opcion no valida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Ingrese un numero valido.");
                scanner.nextLine(); // Limpiar buffer
                opcion = 0;
            }
        } while (opcion != 9 && sesionIniciada);
    }

    private static void ingresarProducto(Scanner scanner) {
        try {
            System.out.print("Ingrese el codigo del producto: ");
            String codigo = scanner.nextLine();
            System.out.print("Ingrese el nombre del producto: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese el precio del producto: ");
            double precio = scanner.nextDouble();
            System.out.print("Ingrese la cantidad del producto: ");
            int cantidad = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            System.out.print("Ingrese la fecha de vencimiento (YYYY-MM-DD): ");
            String fecha = scanner.nextLine();

            ConexionBD.insertarProducto(codigo, nombre, precio, cantidad, fecha);
        } catch (InputMismatchException e) {
            System.out.println("Error: Formato de entrada invalido.");
            scanner.nextLine(); // Limpiar buffer
        }
    }

    private static void buscarProductoPorCodigo(Scanner scanner) {
        System.out.print("Ingrese el codigo del producto: ");
        String codigo = scanner.nextLine();
        ConexionBD.buscarProducto(codigo);
    }

    private static void buscarProductoPorNombre(Scanner scanner) {
        System.out.print("Ingrese el nombre del producto: ");
        String nombre = scanner.nextLine();
        ConexionBD.buscarProductoPorNombre(nombre);
    }

    private static void modificarProducto(Scanner scanner) {
    try {
        System.out.print("Ingrese el codigo del producto a modificar: ");
        String codigoActual = scanner.nextLine();
        
        // Primero verificamos si el producto existe
        ConexionBD.buscarProducto(codigoActual);
        
        System.out.println("\nIngrese los nuevos datos del producto:");
        System.out.print("Nuevo codigo del producto (Enter para mantener el actual): ");
        String nuevoCodigo = scanner.nextLine();
        // Si no se ingresa un nuevo código, mantenemos el actual
        if (nuevoCodigo.trim().isEmpty()) {
            nuevoCodigo = codigoActual;
        }
        
        System.out.print("Nuevo nombre del producto: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Nuevo precio del producto: ");
        double precio = scanner.nextDouble();
        
        System.out.print("Nueva cantidad en stock: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        ConexionBD.actualizarProducto(codigoActual, nuevoCodigo, nombre, precio, cantidad);
    } catch (InputMismatchException e) {
        System.out.println("Error: Formato de entrada invalido.");
        scanner.nextLine(); // Limpiar buffer
    }
}

    private static void eliminarProducto(Scanner scanner) {
        System.out.print("Ingrese el codigo del producto a eliminar: ");
        String codigo = scanner.nextLine();
        ConexionBD.eliminarProducto(codigo);
    }

    private static void realizarVenta(Scanner scanner) {
        try {
            System.out.print("Ingrese el codigo del producto: ");
            String codigo = scanner.nextLine();
            System.out.print("Ingrese la cantidad a vender: ");
            int cantidad = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            ConexionBD.realizarVenta(codigo, cantidad);
        } catch (InputMismatchException e) {
            System.out.println("Error: Formato de entrada invalido.");
            scanner.nextLine(); // Limpiar buffer
        }
    }
}
