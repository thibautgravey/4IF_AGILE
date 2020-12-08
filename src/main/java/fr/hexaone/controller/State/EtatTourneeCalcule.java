package fr.hexaone.controller.State;

import fr.hexaone.controller.Command.ModifierPlanningCommand;
import fr.hexaone.controller.Command.SupprimerDemandeCommand;
import fr.hexaone.controller.Command.SupprimerRequeteCommand;
import fr.hexaone.controller.Controleur;
import fr.hexaone.model.Demande;
import fr.hexaone.model.Requete;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Implémentation d'un State représentant l'état de l'application lorsqu'une
 * tournée est calculé dans l'application
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatTourneeCalcule implements State {

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Controleur c) {
        c.getFenetre().getFenetreControleur().getDurationField().setVisible(false);
        c.getFenetre().getFenetreControleur().getDurationLabel().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonValiderModificationDemande().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonAnnuler().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonLancer().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoutonNouvelleRequete().setVisible(true);
        c.getFenetre().getFenetreControleur().getBoutonValider().setVisible(false);
        c.getFenetre().getFenetreControleur().getDeliveryDurationField().setVisible(false);
        c.getFenetre().getFenetreControleur().getPickUpDurationField().setVisible(false);
        c.getFenetre().getFenetreControleur().getPickUpDurationLabel().setVisible(false);
        c.getFenetre().getFenetreControleur().getDeliveryDurationLabel().setVisible(false);
        c.getFenetre().getFenetreControleur().getBoxBoutonsValiderAnnuler().setVisible(true);
        c.getFenetre().getVueTextuelle().getRequetesControleur().setDraggable(true);

        c.getFenetre().getVueTextuelle().showContextualMenu(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void lancerCalcul(Controleur c) {
        c.getPlanning().calculerMeilleurTournee();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ajoutNouvelleRequete(Controleur c) {
        c.setEtatAjoutNouvelleRequete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void supprimerDemande(Controleur c, Demande demande) {

        if (demande == null) {
            System.out.println("Il faut sélectionner une demande avant.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mauvaise sélection");
            alert.setHeaderText(null);
            alert.setContentText("Il faut selectionner une demande avant.");
            alert.show();
            return;
        }

        // TODO : Créer un marqeur car point orphelin

        // TODO : récupérer depuis la vue graphique/textuelle l'index
        if (c.getListOfCommands().add(new SupprimerDemandeCommand(c.getPlanning(), demande))) {
            c.resetDemandeSelectionnee();
            c.getFenetre().rafraichir(c.getPlanning(), c.getDemandeSelectionnee(), false);
        } else {
            Alert messageAlerte = new Alert(AlertType.INFORMATION);
            messageAlerte.setTitle("Information");
            messageAlerte.setHeaderText(null);
            messageAlerte.setContentText("Au moins une de vos demandes est innaccessible");
            messageAlerte.showAndWait();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void supprimerRequete(Controleur c, Requete requete) {

        if (requete == null) {
            System.out.println("Il faut sélectionner une requete avant.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mauvaise sélection");
            alert.setHeaderText(null);
            alert.setContentText("Il faut selectionner une requete avant.");
            alert.show();
            return;
        }

        if (c.getListOfCommands().add(new SupprimerRequeteCommand(c.getPlanning(), requete))) {
            c.resetDemandeSelectionnee();
            c.getFenetre().rafraichir(c.getPlanning(), c.getDemandeSelectionnee(), false);
        } else {
            Alert messageAlerte = new Alert(AlertType.INFORMATION);
            messageAlerte.setTitle("Information");
            messageAlerte.setHeaderText(null);
            messageAlerte.setContentText("Au moins une de vos demandes est innaccessible");
            messageAlerte.showAndWait();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifierDemande(Controleur c, Demande d) {
        if (d == null) {
            System.out.println("Il faut sélectionner une demande à modifier.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mauvaise sélection");
            alert.setHeaderText(null);
            alert.setContentText("Il faut selectionner une demande à modifier.");
            alert.show();
            return;

        }
        c.setEtatModifierDemande(d.getDuree());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifierPlanning(Controleur c, int i, int j) {
        if (!c.getListOfCommands().add(new ModifierPlanningCommand(c.getPlanning(), i, j))) {
            Alert messageAlerte = new Alert(AlertType.INFORMATION);
            messageAlerte.setTitle("Information");
            messageAlerte.setHeaderText(null);
            messageAlerte.setContentText("Au moins une de vos demandes est innaccessible");
            messageAlerte.showAndWait();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectionnerDemande(Controleur c, Demande demandeSelectionnee) {
        if (c.getDemandeSelectionnee() == demandeSelectionnee)
            c.setDemandeSelectionnee(null);
        else
            c.setDemandeSelectionnee(demandeSelectionnee);
        c.rafraichirVues(false);
    }
}
