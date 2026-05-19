package ru.mephi.vikingdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class VikingLambdaService {

    private final VikingService vikingService;

    @Autowired
    public VikingLambdaService(VikingService vikingService) {
        this.vikingService = vikingService;
    }

    private long countByPredicate(Predicate<Viking> predicate) {
        return vikingService.findAll()
                .stream()
                .filter(predicate)
                .count();
    }

    public long countOlderThan(int age) {
        return countByPredicate(v -> v.age() > age);
    }

    public long countYoungerThan(int age) {
        return countByPredicate(v -> v.age() < age);
    }

    public long countInAgeRange(int minAge, int maxAge) {
        return countByPredicate(v -> v.age() >= minAge && v.age() <= maxAge);
    }

    public long countOutsideAgeRange(int minAge, int maxAge) {
        return countByPredicate(v -> v.age() < minAge || v.age() > maxAge);
    }

    public long countByBeardAndHair(BeardStyle beardStyle, HairColor hairColor) {
        Predicate<Viking> beardPredicate = v -> v.beardStyle() == beardStyle;
        Predicate<Viking> hairPredicate = v -> v.hairColor() == hairColor;
        return countByPredicate(beardPredicate.and(hairPredicate));
    }

    public long countWithOneOrTwoAxes() {
        Predicate<Viking> oneAxe = v -> v.equipment().stream()
                .filter(e -> e.name().equalsIgnoreCase("Axe"))
                .count() == 1;
        Predicate<Viking> twoAxes = v -> v.equipment().stream()
                .filter(e -> e.name().equalsIgnoreCase("Axe"))
                .count() == 2;
        return countByPredicate(oneAxe.or(twoAxes));
    }

    public Optional<Viking> getRandomTallViking() {
        return vikingService.findAll()
                .stream()
                .filter(v -> v.heightCm() > 180)
                .findAny();
    }

    public List<Viking> getAllWithLegendaryEquipment() {
        return vikingService.findAll()
                .stream()
                .filter(v -> v.equipment()
                        .stream()
                        .anyMatch(e -> "Legendary".equalsIgnoreCase(e.quality()))
                )
                .collect(Collectors.toList());
    }

    public List<Viking> getRedBeardedSortedByAge() {
        Predicate<Viking> hasRedHair = v -> v.hairColor() == HairColor.Red;
        Predicate<Viking> hasBeard = v -> v.beardStyle() != BeardStyle.CLEAN_SHAVEN;

        return vikingService.findAll()
                .stream()
                .filter(hasRedHair.and(hasBeard))
                .sorted(Comparator.comparingInt(Viking::age))
                .collect(Collectors.toList());
    }

    public Optional<Integer> findMaxId() {
        List<Viking> all = vikingService.findAll();
        return all.isEmpty()
                ? Optional.empty()
                : Optional.of(all.size() - 1);
    }

    public List<Integer> findEvenIds() {
        List<Viking> all = vikingService.findAll();
        return java.util.stream.IntStream.range(0, all.size())
                .filter(i -> i % 2 == 0)
                .boxed()
                .collect(Collectors.toList());
    }

    public <T> List<T> mapVikings(Function<Viking, T> mapper) {
        return vikingService.findAll()
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    public List<String> getAllNames() {
        return mapVikings(Viking::name);
    }

    public List<Integer> getAllAges() {
        return mapVikings(Viking::age);
    }
}