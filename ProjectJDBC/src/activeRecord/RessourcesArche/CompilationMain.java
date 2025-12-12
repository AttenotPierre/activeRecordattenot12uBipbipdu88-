package activeRecord;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * cette classe a juste pour objectif de verifier les noms des methodes
 */
classe publique CompilationMain {

	public static void main(String[] args) throws SQLException, RealisateurAbsentException {
		///////////////// personnel de test
		
		
		//création de la table Personne
		Personne.createTable();
				
		
		//constructeur
		Personne p;
		p=nouvelle Personne("spielberg", "steven");
		p.sauvegarder();
		p=nouvelle Personne("scott", "ridley");
		p.sauvegarder();
		p=nouvelle Personne("scott", "ridley");
		System.out.println("**** table cree et tuples ajoutés ***");
		
		
		//recherché
		System.out.println("**** tuples présents - findall ****");
		ArrayList<Personne> liste=Personne.findAll();
		pour (Personne plis:liste)
			System.out.println(plits);
		
		//ajout
		System.out.println("**** ajout de David fincher - save ****");
		p=nouvelle Personne("fincher", "david");
		p.sauvegarder();
		
		//récupération
		System.out.println("**** récupération steven Spielberg - findbyid ****");
		Personne temp=Personne.findById(1);
		System.out.println(temp);
		
		//suppression
		System.out.println("**** suppression steven spielberg - supprimer ****");
		temp.supprimer();
		ArrayList<Personne> liste2=Personne.findAll();
		pour (Personne plis:liste2)
			System.out.println(plits);
		
	
		//recherche fincher
		System.out.println("**** recherche fincher - findbyname ****");
		temp.supprimer();
		ArrayList<Personne> liste3=Personne.findByName("fincher");
		pour (Personne plis:liste3)
			System.out.println(plits);
		
		
		//modification
		Personne p2=liste3.get(0);
		p2.setNom("f_i_n_c_h_e_r");
		p2.setPrenom("daviv");
		p2.sauvegarder();
		System.out.println("**** modification de test - enregistrer *** ");
		ArrayList<Personne> liste4=Personne.findAll();
		pour (Personne plis:liste4)
			System.out.println(plits);
		
		//getter
		p2.getId();
		p2.getNom();
		p2.getPrenom();

				
	
		
		///////////////// Film test
		
		//créer une table
		Film.createTable();
		
		Film f=nouveau Film("sept", p2);
		//sauvegarde
		f.sauvegarder();
		
		//finByID
		System.out.println("**** film finbyid **** ");
		Film f2=Film.findById(1);
		System.out.println(f2);
		System.out.println(f2.getId());
		System.out.println(f2.getTitre());
		Personne p3=f2.getRealisateur();
		System.out.println(p3);
		
		//setter
		f2.setTitre("test2");
		f2.sauvegarder();
		
		//sauve film modifier
		System.out.println("**** film modifier **** ");
		Film f3=Film.findById(1);
		System.out.println(f3);
		System.out.println(f3.getId());
		System.out.println(f3.getTitre());
		Personne p5=f3.getRealisateur();
		System.out.println(p5);
		
		
		//film de table Supprime
		Film.deleteTable();
		//suppression de la table personne
		Personne.supprimerTable();
		

		
	}
	
}