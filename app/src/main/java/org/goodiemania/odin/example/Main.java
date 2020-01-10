package org.goodiemania.odin.example;

import java.util.List;
import java.util.UUID;
import org.goodiemania.odin.external.EntityManager;
import org.goodiemania.odin.external.Odin;
import org.goodiemania.odin.external.model.SearchTerm;


public class Main {
    /**
     * Main method for test invoking Odin.
     *
     * @param args Arguments passed to the main method
     */
    public static void main(String[] args) {
        String id = UUID.randomUUID().toString();

        ExampleEntity exampleEntity = new ExampleEntity();
        exampleEntity.setId(id);
        exampleEntity.setName("Example Entity");
        exampleEntity.setDescription("This is a description so YAY");
        exampleEntity.getMap().put("Example", "Howdy");

        Odin odin = Odin.create()
                .addPackageName("org.goodiemania.odin.example")
                .setJdbcConnectUrl("jdbc:sqlite:mainDatabase")
                .build();
        EntityManager<ExampleEntity> em = odin.createFor(ExampleEntity.class);

        em.save(exampleEntity);

        em.getById(id)
                .ifPresent(byId -> {
                    System.out.println(byId.getId());
                    System.out.println(byId.getName());
                    System.out.println(byId.getDescription());
                });

        exampleEntity.setDescription("This is a new Example description!");
        em.save(exampleEntity);

        System.out.println("-------------------------");
        em.getById(id)
                .ifPresent(byId -> {
                    System.out.println(byId.getId());
                    System.out.println(byId.getName());
                    System.out.println(byId.getDescription());
                });

        List<ExampleEntity> searchResults = em.search(List.of(SearchTerm.of("%", "%YAY")));


        System.out.println("-------------------------");
        System.out.println("Search results for YAY:");
        searchResults.forEach(searchResult ->
                System.out.println("Search result: " + searchResult.getName()
                        + "; " + searchResult.getDescription())
        );


        System.out.println("-------------------------");
        System.out.println("Search results for Example:");
        searchResults = em.search(List.of(SearchTerm.of("%", "%Example%")));

        searchResults.forEach(searchResult ->
                System.out.println("Search result: " + searchResult.getName()
                        + "; " + searchResult.getDescription())
        );

        em.deleteById(id);
        if (em.getById(id).isEmpty()) {
            System.out.println("Successfully did NOT find the entity");
        }
    }
}
