import navigator.*;
import java.io.*;
import java.util.*;

class Test {
    public final int ETUDIANT_MAX = 100, MATIERE_MAX = 10;
    public final float ABSENT = -1.0f; 

    public class Etudiant {
        String[] noms_etu = new String[ETUDIANT_MAX];
        int nb_etu = 0;
    }

    public class Matiere {
        String[] nom_matieres = new String[MATIERE_MAX];
        boolean[] affichee = new boolean[MATIERE_MAX]; // Pour masquer/afficher
        int nb_matiere = 0;
    }

    Scanner in = new Scanner(System.in);
    Navigator nav = new Navigator(); 
    float[][] notes = new float[MATIERE_MAX][ETUDIANT_MAX];
    int[] coef_mat = new int[MATIERE_MAX];

    // --- CALCULS ---

    float moyenne_etu(int id_etu, Matiere mat) {
        float somme = 0;
        float total_coef = 0;
        for (int i = 0; i < mat.nb_matiere; i++) {
            if (notes[i][id_etu] != ABSENT) {
                somme += notes[i][id_etu] * coef_mat[i];
                total_coef += coef_mat[i];
            }
        }
        return (total_coef == 0) ? 0 : (somme / total_coef);
    }

    float moyenne_matiere(int id_mat, Etudiant etu) {
        float somme = 0;
        int nb_presents = 0;
        for (int j = 0; j < etu.nb_etu; j++) {
            if (notes[id_mat][j] != ABSENT) {
                somme += notes[id_mat][j];
                nb_presents++;
            }
        }
        return (nb_presents == 0) ? 0 : (somme / nb_presents);
    }

    // --- LOGIQUE DE TRI ---

    int[] obtenirIndicesTriesMoyenne(Etudiant etu, Matiere mat) {
        int[] indices = new int[etu.nb_etu];
        for (int i = 0; i < etu.nb_etu; i++) indices[i] = i;
        for (int i = 0; i < etu.nb_etu - 1; i++) {
            for (int j = i + 1; j < etu.nb_etu; j++) {
                if (moyenne_etu(indices[j], mat) > moyenne_etu(indices[i], mat)) {
                    int temp = indices[i]; indices[i] = indices[j]; indices[j] = temp;
                }
            }
        }
        return indices;
    }

    int[] obtenirIndicesTriesMatiere(int id_mat, Etudiant etu) {
        int[] indices = new int[etu.nb_etu];
        for (int i = 0; i < etu.nb_etu; i++) indices[i] = i;
        for (int i = 0; i < etu.nb_etu - 1; i++) {
            for (int j = i + 1; j < etu.nb_etu; j++) {
                if (notes[id_mat][indices[j]] > notes[id_mat][indices[i]]) {
                    int temp = indices[i]; indices[i] = indices[j]; indices[j] = temp;
                }
            }
        }
        return indices;
    }

    // --- GESTION DONNEES ---

    void viderDonnees(Etudiant etu, Matiere mat) {
        etu.nb_etu = 0;
        mat.nb_matiere = 0;
        for (int i = 0; i < MATIERE_MAX; i++) {
            mat.affichee[i] = true;
            for (int j = 0; j < ETUDIANT_MAX; j++) notes[i][j] = ABSENT;
        }
    }

    void sauvegarderDansFichier(String nomFichier, Etudiant etu, Matiere mat) {
        try {
            PrintWriter writer = new PrintWriter(new File(nomFichier));
            writer.println(etu.nb_etu + ";" + mat.nb_matiere);
            for (int i = 0; i < mat.nb_matiere; i++) writer.println(mat.nom_matieres[i] + ";" + coef_mat[i]);
            for (int j = 0; j < etu.nb_etu; j++) {
                writer.print(etu.noms_etu[j]);
                for (int i = 0; i < mat.nb_matiere; i++) writer.print(";" + notes[i][j]);
                writer.println();
            }
            writer.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    void lireDepuisFichier(String nomFichier, Etudiant etu, Matiere mat) {
        try {
            Scanner scanner = new Scanner(new File(nomFichier));
            if (scanner.hasNextLine()) {
                String[] tailles = scanner.nextLine().split(";");
                etu.nb_etu = Integer.parseInt(tailles[0]);
                mat.nb_matiere = Integer.parseInt(tailles[1]);
                for (int i = 0; i < mat.nb_matiere; i++) {
                    String[] mInfo = scanner.nextLine().split(";");
                    mat.nom_matieres[i] = mInfo[0];
                    coef_mat[i] = Integer.parseInt(mInfo[1]);
                    mat.affichee[i] = true;
                }
                for (int j = 0; j < etu.nb_etu; j++) {
                    String[] eInfo = scanner.nextLine().split(";");
                    etu.noms_etu[j] = eInfo[0];
                    for (int i = 0; i < mat.nb_matiere; i++) notes[i][j] = Float.parseFloat(eInfo[i + 1]);
                }
            }
            scanner.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    // --- EXECUTION ---

    void run() {
        Etudiant etu = new Etudiant();
        Matiere mat = new Matiere();
        viderDonnees(etu, mat);

        while (true) {
            nav.beginPage();
            nav.println("<html><head><meta charset='UTF-8'><style>");
            nav.println("body { font-family: sans-serif; margin: 40px; background: #f4f7f6; }");
            nav.println("nav { background: #2c3e50; padding: 10px; border-radius: 5px; margin-bottom: 20px; }");
            nav.println("nav a { color: white; text-decoration: none; margin-right: 15px; font-weight: bold; }");
            nav.println("table { border-collapse: collapse; width: 100%; background: white; margin-bottom: 10px;}");
            nav.println("th, td { border: 1px solid #ddd; padding: 10px; text-align: center; }");
            nav.println("th { background: #34495e; color: white; }");
            nav.println(".btn-danger { background: #d9534f; color: white; border: none; padding: 8px; border-radius: 4px; }");
            nav.println("form { background: white; padding: 20px; border: 1px solid #ddd; border-radius: 5px; margin-bottom: 15px; }");
            nav.println("hr { border: 0; height: 1px; background: #333; margin: 30px 0; }");
            nav.println("</style></head><body>");

            nav.println("<nav><a href='/'>Consulter</a> <a href='/edit'>Saisir</a> <a href='/add'>Ajouts</a> <a href='/etu'>Étudiant</a> <a href='/plus'>Plus</a></nav>");

            String path = nav.getPath();

            if (path.equals("/edit")) {
                if (nav.containsKey("sauvegarder")) {
                    for (int i = 0; i < etu.nb_etu; i++) {
                        for (int j = 0; j < mat.nb_matiere; j++) {
                            String k = "n_" + i + "_" + j;
                            if (nav.get(k).equalsIgnoreCase("ABS")) notes[j][i] = ABSENT;
                            else if (nav.isDouble(k)) notes[j][i] = (float) nav.getDouble(k);
                        }
                    }
                }
                nav.println("<h2>Saisie des notes</h2><form method='POST'><table>");
                for (int i = 0; i < etu.nb_etu; i++) {
                    nav.print("<tr><td>" + etu.noms_etu[i] + "</td>");
                    for (int j = 0; j < mat.nb_matiere; j++) {
                        String v = (notes[j][i] == ABSENT) ? "ABS" : "" + notes[j][i];
                        nav.print("<td>" + mat.nom_matieres[j] + " <input type='text' size='4' name='n_" + i + "_" + j + "' value='" + v + "'></td>");
                    }
                    nav.println("</tr>");
                }
                nav.println("</table><br><input type='submit' name='sauvegarder' value='Enregistrer'></form>");
            } 
            else if (path.equals("/add")) {
                if (nav.containsKey("nom_etu")) { etu.noms_etu[etu.nb_etu] = nav.get("nom_etu"); etu.nb_etu++; }
                if (nav.containsKey("nom_mat") && nav.isInt("coef_mat")) { 
                    mat.nom_matieres[mat.nb_matiere] = nav.get("nom_mat"); 
                    coef_mat[mat.nb_matiere] = nav.getInt("coef_mat"); 
                    mat.affichee[mat.nb_matiere] = true;
                    mat.nb_matiere++; 
                }
                if (nav.containsKey("f_save")) sauvegarderDansFichier(nav.get("nom_fichier"), etu, mat);
                if (nav.containsKey("f_load")) lireDepuisFichier(nav.get("nom_fichier"), etu, mat);
                if (nav.containsKey("vider")) viderDonnees(etu, mat);

                nav.println("<form method='POST'><h3>Fichiers</h3> Nom: <input type='text' name='nom_fichier'> <input type='submit' name='f_save' value='Sauvegarder'> <input type='submit' name='f_load' value='Lire'></form>");
                nav.println("<form method='POST'><h3>Ajouts</h3> Elève: <input type='text' name='nom_etu'> <input type='submit' value='Ajouter'><br><br> Matière: <input type='text' name='nom_mat'> Coef: <input type='text' size='2' name='coef_mat'> <input type='submit' value='Ajouter'></form>");
                nav.println("<form method='POST'><h3>Danger</h3> <input type='submit' name='vider' value='Tout effacer' class='btn-danger'></form>");
            }
            else if (path.equals("/plus")) {
                nav.println("<h2>Plus</h2>");

                // --- CHOIX DES MATIÈRES À AFFICHER ---
                if (nav.containsKey("valider_affichage")) {
                    for (int i = 0; i < mat.nb_matiere; i++) {
                        mat.affichee[i] = nav.containsKey("visib_" + i);
                    }
                }
                nav.println("<form method='POST'><h3>Choisir les matières à afficher</h3>");
                for (int i = 0; i < mat.nb_matiere; i++) {
                    String checked = mat.affichee[i] ? "checked" : "";
                    nav.println("<input type='checkbox' name='visib_" + i + "' " + checked + "> " + mat.nom_matieres[i] + "<br>");
                }
                nav.println("<br><br><input type='submit' name='valider_affichage' value='Mettre à jour'></form>");
                nav.println("<hr>");

                // --- MOYENNES PAR MATIÈRE ---
                nav.println("<h3>Moyennes de classe par matière</h3>");
                for (int i = 0; i < mat.nb_matiere; i++) {
                    nav.println("<p>" + mat.nom_matieres[i] + " : <b>" + String.format("%.2f", moyenne_matiere(i, etu)) + "</b></p>");
                }
                nav.println("<hr>");

                // --- CLASSEMENT GÉNÉRAL ---
                nav.println("<h3>Classement par moyenne générale</h3><table>");
                int[] triGen = obtenirIndicesTriesMoyenne(etu, mat);
                for (int i = 0; i < etu.nb_etu; i++) {
                    int idx = triGen[i];
                    nav.println("<tr><td>" + (i + 1) + "</td><td>" + etu.noms_etu[idx] + "</td><td>" + String.format("%.2f", moyenne_etu(idx, mat)) + "</td></tr>");
                }
                nav.println("</table>");
                nav.println("<hr>");

                // --- CLASSEMENT PAR MATIÈRE ---
                nav.println("<form method='POST'><h3>Classement par matière spécifique</h3><select name='m_tri'>");
                for (int i = 0; i < mat.nb_matiere; i++) nav.println("<option value='" + i + "'>" + mat.nom_matieres[i] + "</option>");
                nav.println("</select> <input type='submit' name='do_tri' value='Trier'></form>");
                if (nav.containsKey("do_tri") && nav.isInt("m_tri")) {
                    int m = nav.getInt("m_tri");
                    nav.println("<h4>Résultats pour " + mat.nom_matieres[m] + "</h4><table>");
                    int[] triMat = obtenirIndicesTriesMatiere(m, etu);
                    for (int i = 0; i < etu.nb_etu; i++) {
                        int idx = triMat[i];
                        nav.println("<tr><td>" + (i + 1) + "</td><td>" + etu.noms_etu[idx] + "</td><td>" + (notes[m][idx] == ABSENT ? "ABS" : notes[m][idx]) + "</td></tr>");
                    }
                    nav.println("</table>");
                }
            }
            else if (path.equals("/etu")) {
                nav.println("<h2>Consulter un étudiant</h2><form method='POST'><select name='id_etu'>");
                for (int i = 0; i < etu.nb_etu; i++) nav.println("<option value='" + i + "'>" + etu.noms_etu[i] + "</option>");
                nav.println("</select> <input type='submit' value='Afficher'></form>");
                if (nav.isInt("id_etu")) {
                    int id = nav.getInt("id_etu");
                    nav.println("<h3>Notes de " + etu.noms_etu[id] + "</h3><table><tr>");
                    for (int i = 0; i < mat.nb_matiere; i++) nav.println("<th>" + mat.nom_matieres[i] + "</th>");
                    nav.println("<th>Moyenne</th></tr><tr>");
                    for (int i = 0; i < mat.nb_matiere; i++) nav.println("<td>" + (notes[i][id] == ABSENT ? "ABS" : notes[i][id]) + "</td>");
                    nav.println("<td><b>" + String.format("%.2f", moyenne_etu(id, mat)) + "</b></td></tr></table>");
                }
            }
            else {
                nav.println("<h2>Consultation Générale</h2><table><tr><th>Étudiant</th>");
                for (int i = 0; i < mat.nb_matiere; i++) {
                    if (mat.affichee[i]) nav.println("<th>" + mat.nom_matieres[i] + "</th>");
                }
                nav.println("<th>Moyenne</th></tr>");
                for (int j = 0; j < etu.nb_etu; j++) {
                    float m = moyenne_etu(j, mat);
                    nav.println("<tr " + (m < 10 ? "style='background:#f8d7da;'" : "") + "><td>" + etu.noms_etu[j] + "</td>");
                    for (int i = 0; i < mat.nb_matiere; i++) {
                        if (mat.affichee[i]) {
                            String v = (notes[i][j] == ABSENT) ? "ABS" : "" + notes[i][j];
                            nav.println("<td>" + v + "</td>");
                        }
                    }
                    nav.println("<td><b>" + String.format("%.2f", m) + "</b></td></tr>");
                }
                nav.println("</table>");
            }
            nav.println("</body></html>");
            nav.endPage();
        }
    }

    public static void main(String[] args) { new Test().run(); }
}