package com.example.foyerrouamnissi.Services.Bloc;

import com.example.foyerrouamnissi.DAO.Entities.Bloc;
import com.example.foyerrouamnissi.DAO.Entities.Chambre;
import com.example.foyerrouamnissi.DAO.Entities.Foyer;
import com.example.foyerrouamnissi.DAO.Repositories.BlocRepository;
import com.example.foyerrouamnissi.DAO.Repositories.ChambreRepository;
import com.example.foyerrouamnissi.DAO.Repositories.FoyerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.*;

@Service
@AllArgsConstructor
public class BlocService implements IBlocService {

    BlocRepository blocRepository;
    ChambreRepository chambreRepository;
    FoyerRepository foyerRepository;


    @Override
    public Bloc addBloc(Bloc b) {
        return blocRepository.save(b);
    }

    @Override
    public List<Bloc> addBlocs(List<Bloc> b) {
        return blocRepository.saveAll(b);//on ajoute des lignes dans la bd
    }

    @Override
    public Bloc editBloc(Bloc b) {
        if (blocRepository.existsById(b.getIdBloc())) {
            return blocRepository.save(b);
        }
        return null ;
    }

    @Override
    public List<Bloc> findAll() {
        return blocRepository.findAll();
    }

    @Override
    public Bloc findById(long id) {
        return blocRepository.findById(id).get();
    }

    @Override
    public void deleteById(long id) {
        blocRepository.deleteById(id);
    }

    @Override
    public void delete(Bloc b) {
        blocRepository.delete(b);
    }

    @Override
    public Bloc affecterChambresABloc(List<Long> numChambre, String nomBloc) {
        //Cascade
        Bloc bloc = blocRepository.findByNomBloc(nomBloc);
        //creation ensembre vide pour stocker les instances de chambres
        Set<Chambre> chambres = new HashSet<>();
        for (long numC : numChambre) {
            Chambre c = chambreRepository.findByNumeroChambre(numC);
            chambres.add(c);
            //affecter child (chambre) au parent (bloc)
            c.setBloc(bloc);
            //sauvegarde
            chambreRepository.save(c);
        }
        bloc.setChambres(chambres);
        //mise à jour et sauvegarde
        blocRepository.save(bloc);
        return bloc;
    }



    @Override
    public void createBlocWithFoyer(long capaciteBloc, String nomBloc, long idFoyer) {
// Create a new Chambre object
        Bloc bloc = new Bloc();
        bloc.setCapaciteBloc(capaciteBloc);
        bloc.setNomBloc(nomBloc);

        // Retrieve the corresponding Bloc object from the database
        Foyer foyer = foyerRepository.findById(idFoyer).orElse(null);
        if (foyer != null) {
            // Set the bloc property of the Chambre object
            bloc.setFoyer(foyer);
        }

        // Save the Chambre object to the database
        blocRepository.save(bloc);
    }

    @Override
    public List<Bloc> getBlocParNomFoyer(String nomFoyer) {
        return blocRepository.findByFoyer_NomFoyer(nomFoyer);
    }

    @Override
    public List<Bloc> searchBlocsByBlocName(String nomBloc) {
        return null;
    }

    @Override
    public List<Bloc> searchBlocsByfoyer(Long idfoyer) {
        List<Bloc> blocs = blocRepository.searchBlocsByIdFoyer(idfoyer);
        for (Bloc bloc : blocs) {
            Foyer foyer = bloc.getFoyer();
            if (bloc != null) {
                String nomFoyer = foyer.getNomFoyer();
            }
        }
        return blocs;
    }

    @Override
    public List<Chambre> getChambresByBlocId(Long idBloc) {
        Bloc bloc = blocRepository.findById(idBloc).orElse(null);
        if (bloc != null) {
            return new ArrayList<>(bloc.getChambres());
        }
        return Collections.emptyList();
    }

    @Override
    public Foyer getFoyerByBlocId(Long idBloc) {
        Bloc bloc = blocRepository.findById(idBloc).orElse(null);
        if (bloc != null) {
            return bloc.getFoyer(); // Assuming the getFoyer() method exists in the Bloc entity
        }
        return null; // or throw an exception if appropriate
    }


}




