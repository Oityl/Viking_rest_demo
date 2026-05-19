package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.Viking;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class VikingService {
    private final CopyOnWriteArrayList<Viking> vikings = new CopyOnWriteArrayList<>();
    private final VikingFactory vikingFactory;

    @Autowired
    public VikingService(VikingFactory vikingFactory) {
        this.vikingFactory = vikingFactory;
    }

    public List<Viking> findAll() {
        return List.copyOf(vikings);
    }

    public Viking createRandomViking() {
        Viking viking = vikingFactory.createRandomViking();
        vikings.add(viking);
        return viking;
    }

    public Viking addViking(Viking viking) {
        vikings.add(viking);
        return viking;
    }

    public boolean removeViking(String name) {
        Optional<Viking> found = vikings.stream()
                .filter(v -> v.name().equalsIgnoreCase(name))
                .findFirst();
        if (found.isPresent()) {
            vikings.remove(found.get());
            return true;
        }
        return false;
    }

    public Optional<Viking> updateViking(String name, Viking updated) {
        for (int i = 0; i < vikings.size(); i++) {
            if (vikings.get(i).name().equalsIgnoreCase(name)) {
                vikings.set(i, updated);
                return Optional.of(updated);
            }
        }
        return Optional.empty();
    }

    public List<Viking> generateBulkVikings(int count) {
        List<Viking> generated = IntStream.range(0, count)
                .mapToObj(i -> vikingFactory.createRandomViking())
                .collect(Collectors.toList());

        generated.forEach(vikings::add);
        return generated;
    }
}