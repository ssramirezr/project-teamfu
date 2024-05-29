import java.util.*;

public class Main {

    private static Map<String, Set<String>> First(Map<String, List<String[]>> gramatica) {
        //se crea un mapa con los no terminales como key y va almacenando los first como value
        Map<String, Set<String>> first = new HashMap<>();

        //sellena first para cada no terminal con un conjunto vacio
        for (String noTerminal : gramatica.keySet()) {
            first.put(noTerminal, new HashSet<>());
        }

        boolean cambio = true;
        while (cambio) {
            cambio = false;
            for (String noTerminal : gramatica.keySet()) {
                for (String[] produccion : gramatica.get(noTerminal)) { //recorre las producciones de cada no terminal
                    for (String simbolo : produccion) { //recorre los simbolos de cada produccion
                        if (!Character.isUpperCase(simbolo.charAt(0)) || simbolo.equals("e")) { //si el caracter de la iterancion en produccion no es mayuscula o es epsilon
                            if (first.get(noTerminal).add(simbolo)) {//añade el simbolo al conjunto first del no terminal
                                cambio = true;
                            }
                            break;
                        } else {
                            first.get(noTerminal).addAll(first.get(simbolo));//si el simbolo de la produccion es otro no terminal añade todos los first de este
                            if (first.get(simbolo).contains("e")) { //se asegura si puede siguir con el siguiente simbolo de la produccion si el first del no terminal contiene epsilon
                                continue;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return first;
    }

    private static Map<String, Set<String>> Follow(Map<String, List<String[]>> gramatica, Map<String, Set<String>> first) {
        //se crea un mapa con los no terminales como key y va almacenando los follow como value
        Map<String, Set<String>> follow = new HashMap<>();

        //se llena follow para cada no terminal con un conjunto vacio
        for (String noTerminal : gramatica.keySet()) {
            follow.put(noTerminal, new HashSet<>());
        }
        //se pone $ ya que este siempre pertece al follow de S
        follow.get("S").add("$");

        boolean cambio = true;
        while (cambio) {
            cambio = false;
            for (String noTerminal : gramatica.keySet()) {
                for (String[] produccion : gramatica.get(noTerminal)) {
                    Set<String> follow_nt = new HashSet<>(follow.get(noTerminal)); //se crea un conjunto con el follow del no terminal para poder añadirlo al follow de los no terminales de la produccion
                    for (int i = produccion.length - 1; i >= 0; i--) { //recorre la produccion de derecha a izquierda para al encontrar un no terminal poder añadir el first del simbolo anterior al follow del no terminal
                        String simbolo = produccion[i];
                        if (Character.isUpperCase(simbolo.charAt(0))) { //revisa si es otro no terminal
                            if (follow.get(simbolo).addAll(follow_nt)) { //añade el follow del no terminal al follow del no terminal de la produccione
                                cambio = true;
                            }
                            if (first.get(simbolo).contains("e")) { //si el first del no terminal de la produccion contiene epsilon
                                follow_nt.addAll(first.get(simbolo)); //añade el first del no terminal al follow del no terminal de la produccion
                                follow_nt.remove("e");
                            } else {
                                follow_nt = new HashSet<>(first.get(simbolo)); //si no contiene epsilon se crea un nuevo conjunto con el first del no terminal
                            }
                        } else {
                            follow_nt = new HashSet<>(Collections.singleton(simbolo)); //si no es un no terminal se crea un conjunto con el primer terminal que se encuentre, de forma que luego el conjunto no cambie por un terminal que encuentre despues
                        }
                    }
                }
            }
        }
        return follow;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(); //numero de casos
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            int m = sc.nextInt(); // numero de producciones
            sc.nextLine();

            // se crea la gramatica tomando como key el no terminal y como value conjuntos de producciones
            Map<String, List<String[]>> gramatica = new HashMap<>();

            //se llena la gramatica
            for (int j = 0; j < m; j++) {
                String[] produccion = sc.nextLine().split("\\s+");
                String noTerminal = produccion[0];
                List<String[]> listaProducciones = new ArrayList<>();
                for (int k = 1; k < produccion.length; k++) {
                    listaProducciones.add(produccion[k].split(""));
                }
                gramatica.put(noTerminal, listaProducciones);
            }

            Map<String, Set<String>> conjuntosFirst = First(gramatica);
            Map<String, Set<String>> conjuntosFollow = Follow(gramatica, conjuntosFirst);

            System.out.println("Caso " + (i + 1) + ":");

            for (String noTerminal : gramatica.keySet()) { //imprime los conjuntos first de cada no terminal
                System.out.print("First(" + noTerminal + ") = {");
                Set<String> firstSet = conjuntosFirst.get(noTerminal);
                for (String simbolo : firstSet) {
                    System.out.print(simbolo + ",");
                }
                System.out.println("}");
            }
            System.out.println();
            for (String noTerminal : gramatica.keySet()) { // Imprimir conjunto Follow de cada no terminal
                System.out.print("Follow(" + noTerminal + ") = {");
                Set<String> followSet = conjuntosFollow.get(noTerminal);
                for (String simbolo : followSet) {
                    System.out.print(simbolo + ",");
                }
                System.out.println("}");
            }
            System.out.println("______________________________");
        }
    }
}
