package proyectofinbd;

public class Login {
    // Usuarios y contraseñas predefinidos
    private static final String[][] USUARIOS = {
        {"admin", "admin123"},
        {"usuario1", "pass123"},
        {"vendedor", "vend123"}
    };
    
    public static boolean validarUsuario(String usuario, String password) {
        for (String[] credencial : USUARIOS) {
            if (credencial[0].equals(usuario) && credencial[1].equals(password)) {
                return true;
            }
        }
        return false;
    }
}