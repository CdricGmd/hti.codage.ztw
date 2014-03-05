
This repository represents a project in Telecom SudParis 'High Tech Imaging' for the lecture ***Image, video and 3D graphics compression***.

The project is pubished under The MIT License (MIT)
> Copyright (c) 2013 Cédric Golmard, Institut Mines – Telecom ; Telecom SudParis as part of HighTech Imaging major

The subject was proposed by Titus Zaharia, researcher at Institut Mines – Telecom ; ARTEMIS.

As the related lecture was in french, the entire project description and a part of the source comments are also in french.

----

Module ***Image, video and 3D graphics compression*** de Télécom SudParis, voie d'approfondissement *High Tech Imaging*. 

# hti.codage.ztw

## Qu'est-ce que c'est ?

Ce programme Java a pour objectif de d'implémenter une *méthode de codage ZTW* d'images transformée en ondelettes. Ce codage est avec perte (*lossy*) mais permet un codage et décodage progressifs de l'image. Par exemple, si le codage s'arrête à un instant quelcoque, on pourra décoder l'image avec le fichier binaire déjà écrit mais avec une qualité moindre.

![Capture d'écran du programme](http://img15.hostingpics.net/pics/288110Screenshot.png "Capture d'écran du programme")

Le programme dispose d'une interface graphique qui permet :

Partie **codage** :

* d'ouvrir une image format *jpeg* ou *gif*.
* de lui appliquer une transformée en ondelette directe.
* de coder l'image dans un fichier binaire, selon une *méthode de codage ZTW*. Par défaut sous : *"./Images/NomImageOriginale.bitstream"* .

Partie **decodage** :

* d'ouvrir un fichier binaire, par defaut *.bitstream* .
* de décoder ce fichier selon une *méthode de codage ZTW* dans une *image transformee*.
* d'appliquer une transformée en ondelettes inverse pour obtenir l'*image decompressée*

Partie **affichage** :

* afficher l'*image orginale* chargée.
* affichier l'*image transformée*.
* affichier l'*image modifiée*.
* afficher les *histogrammes* et *entropies* de ces images.

Partie **paramètres** :

* Choix du type de transformée en ondelettes.
* Nombre de niveaux de résolution pour la transformée et le codage.
* Taux de compression de l'image souhaité au moment du codage.
* Taille de l'image. **À renseigner** à l'ouverture d'un fichier binaire pour le décodage.

## Utilisation
 
Depuis l'interface graphique, plusieurs scenarii d'utilisation sont possibles :

* ***Codage*** : ouverture d'une image, tranformée directe et codage.
* ***Décodage*** : ouverture d'un fichier binaire, renseignement de la taille de l'image, décodage et transformée inverse.
* On peut enchainer les parties *Codage* et *Décodage*. Sans avoir à ouvrir le fichier qui vient d'être codé (le fichier et la taille de l'image restent chargés en mémoire après codage).

Autres actions possibles :

* Appliquer la *transformée directe* puis *inverse* sans *codage*/*decodage*. (pour utiliser la transformée uniquement).
* Afficher l'*image transformée* modifiée par le *codage* et son *histogramme* après *codage*.
* Appliquer la *transformée inverse* directement après le codage pour voir comment l'image transformée est affectée par le codage.
* Aficher l'*image transformée décodée*  et son *histogramme* après *décodage*.

## Implémentation

L'implémentation utilise en partie du code Java fourni en début de module qui a été modifié pour personnaliser l'interface graphique et les traitements d'image. Il reste néanmoins dans le code source des parties "fantômes" inutilisées du code initial qui peuvent en complexifier (inutilement) la lecture.

Une transformée de Haar 2D multiréolution est implémentée dans la classe *TraitImage*.

A cela se rajoute un *package "Compression"* contenant la classe "*CodageZTW.java*". Le critère d'arrêt implémenté pour ce codage est la taille de fichier binaire voulue. La taille est vérifiée après chaque itération de l'algorithme (un parcours de l'image).

Pour utiliser le *codage ZTW*, vous devez disposer de l''image transformée en ondelettes sous forme d'un tableau de pixels (*double*). Les dimensions de l'image et le nombre de niveaux de résolution de la transformée doivent être connus.

La classe doit être instanciée car elle utilise un *buffer* pour lire et écrire le fichier bianire. Ce buffer est alloué et initialisé à l'instanciation.

Codage

	CodageZTW ztw = new CodageZTW();
	
	// Codage de image_trans dans ""nom_de_fichier_binaire""
	ztw.ztw_code(image_trans, height, width, niv_resol, required size, "./pathTo/nom_de_fichier_binaire"");

Décodage

	CodageZTW ztw = new CodageZTW();
	
	// Décodage du fichier "mon_image.bitstream" dans le tableau image_trans_dec
	ztw.ztw_decode(image_trans_dec, height, width, niv_resol, "./PathTo/mon_image.bitstream");
	
Le codage ZTW est basé sur un étiquetage des pixels des sous-bandes haute-fréquence d'une image transformée. Il existe 4 étiquettes : N, P, ZI et ZTR. On peut donc coder une étiquette sur 2 bits. 

Un buffer de type *int* (32bits) permet de lire et écrire 16 étiquettes (16\*2=32). Or Java ne gère pas les types *bit*, on code alors une étiquette par un *int* dont seul les deux pixels de poids faible ont un sens : l'étiquette. On utilise les opérateurs binaires de Java pour faire du *bit shifting* (décalage de bits) afin d'ajouter une étiquette au *buffer*.

	// Illustration en pseudo-code
	
	// L'étiquette P est 11
	P = OxOOO3 = 0b 0000 0000 0000 0011
	// L'étiqette N est 10
	N = 0x0002 = 0b 0000 0000 0000 0010
	
	// Initialisation du buffer et de la position courante.
	int bitBuffer = 0x0000;
	int posBuffer = 0;
	
	// Ajout de P au buffer
	// Obtenu par bit shifting
	bitBuffer == 0b 0000 0000 0000 0011
	posBuffer += 2;
	// Ajout de N au buffer
	// Obtenu par bit shifting
	bitBuffer == 0b 0000 0000 0000 1000
	posBuffer += 2;
	// Ajout de P au buffer
	// Obtenu par bit shifting
	bitBuffer == 0b 0000 0000 0011 1000
	posBuffer += 2;
	
	// Ecriture
	if(posBuffer == 32){
		myDataOutputStream.writeInt(bitBuffer);
		posBuffer = 0;
	}
	
	// Le processus de lecture est équivalent mais inversé.

## Limitations

- Une seule transformée est disponible : la transformée de Haar 2D multirésolution. 
	- D'autres transformées peuvent être implémentées. Il faut alors dans *menu.java* mettre à jour le champ de la liste déroulante *jComboTransformee* et appeler les transformées directe et inverse dans la méthode *doTransformee()* et *doTransInverse()* dans un bloc conditionel sur le type de transformée demandée.
- La methode *decodeZTW* de *CodageZTW* ne donne pas de résultat satisfaisant. Le décodage échoue ou donne en résultat une image transformee non conforme à ce qui a été codé. On peut présupposer que le problème résulte soit du parcours de l'image soit de la lecture du fichier binaire.
