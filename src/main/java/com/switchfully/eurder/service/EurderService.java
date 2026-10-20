package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.Eurder;
import com.switchfully.eurder.domain.ItemGroup;
import com.switchfully.eurder.repository.EurderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class EurderService {

    private final EurderRepository eurderRepository;
    private final CustomerService customerService;
    private final ItemService itemService;


    public EurderService(EurderRepository eurderRepository, CustomerService customerService, ItemService itemService) {
        this.eurderRepository = eurderRepository;
        this.customerService = customerService;
        this.itemService = itemService;
    }


    public Eurder createEurder(UUID customerId, List<ItemGroupRequest> requests) {
        customerService.getCustomerById(customerId);
        LocalDate orderDate = LocalDate.now();
        List<ItemGroup> itemGroups = requests.stream()
                                             .map(request -> new ItemGroup(itemService.getItemById(request.itemId()),
                                                     request.quantity(),
                                                     orderDate)).toList();

        Eurder eurder = new Eurder(customerId, itemGroups);
        return eurderRepository.save(eurder);

    }
}


/**
 * Crée une commande (Eurder) pour un client existant.
 *
 * LEXIQUE
 * - Eurder           = la commande entière (le "ticket de caisse")
 * - ItemGroup        = UNE LIGNE de la commande : un article + sa quantité
 *                      (ex : "2 x VTT"). Le prix est copié et la date
 *                      d'expédition calculée au moment de la commande.
 * - ItemGroupRequest = ce que le client DEMANDE pour une ligne :
 *                      seulement l'id de l'article + la quantité.
 *
 * LES DEUX LISTES
 *                      | List<ItemGroupRequest>   | List<ItemGroup>
 * ---------------------|--------------------------|------------------------------
 * C'est quoi           | ce que le client demande | les lignes de la commande
 * Contenu              | itemId + quantité        | itemId, quantité,
 *                      |                          | prix copié, date d'expédition
 * Qui la fournit       | le controller            | ce service (via le map)
 *
 * ETAPES
 * 1. Vérifier que le client existe (sinon CustomerNotFoundException).
 * 2. Pour chaque ItemGroupRequest : retrouver l'Item (sinon ItemNotFoundException)
 *    et créer l'ItemGroup correspondant.
 * 3. Créer l'Eurder avec ces ItemGroup, la sauvegarder et la renvoyer.
 */
