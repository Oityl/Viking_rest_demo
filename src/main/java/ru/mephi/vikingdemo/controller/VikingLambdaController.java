package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingLambdaService;
import ru.mephi.vikingdemo.service.VikingService;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Viking Lambda", description = "Операции с лямбда-функциями")
public class VikingLambdaController {

    private final VikingLambdaService lambdaService;
    private final VikingService vikingService;

    public VikingLambdaController(VikingLambdaService lambdaService, VikingService vikingService) {
        this.lambdaService = lambdaService;
        this.vikingService = vikingService;
    }

    @GetMapping("/api/lambda/count/older-than/{age}")
    @Operation(summary = "Количество викингов старше заданного возраста")
    public long countOlderThan(
            @Parameter(description = "Возраст (исключительно)", example = "30")
            @PathVariable int age) {
        return lambdaService.countOlderThan(age);
    }

    @GetMapping("/api/lambda/count/younger-than/{age}")
    @Operation(summary = "Количество викингов моложе заданного возраста")
    public long countYoungerThan(
            @Parameter(description = "Возраст (исключительно)", example = "25")
            @PathVariable int age) {
        return lambdaService.countYoungerThan(age);
    }

    @GetMapping("/api/lambda/count/age-range")
    @Operation(summary = "Количество викингов в возрастном диапазоне [min, max]")
    public long countInRange(
            @Parameter(description = "Минимальный возраст включительно", example = "20")
            @RequestParam int min,
            @Parameter(description = "Максимальный возраст включительно", example = "40")
            @RequestParam int max) {
        return lambdaService.countInAgeRange(min, max);
    }

    @GetMapping("/api/lambda/count/age-outside")
    @Operation(summary = "Количество викингов вне возрастного диапазона [min, max]")
    public long countOutsideRange(
            @Parameter(description = "Минимальная граница диапазона", example = "20")
            @RequestParam int min,
            @Parameter(description = "Максимальная граница диапазона", example = "40")
            @RequestParam int max) {
        return lambdaService.countOutsideAgeRange(min, max);
    }

    @GetMapping("/api/lambda/count/beard-and-hair")
    @Operation(summary = "Количество викингов с заданной формой бороды И цветом волос")
    public long countByBeardAndHair(
            @Parameter(description = "Форма бороды", example = "BRAIDED")
            @RequestParam BeardStyle beardStyle,
            @Parameter(description = "Цвет волос", example = "Red")
            @RequestParam HairColor hairColor) {
        return lambdaService.countByBeardAndHair(beardStyle, hairColor);
    }

    @GetMapping("/api/lambda/count/axes")
    @Operation(summary = "Количество викингов с одним или двумя топорами")
    public long countAxes() {
        return lambdaService.countWithOneOrTwoAxes();
    }

    @GetMapping("/api/lambda/display/random-tall")
    @Operation(summary = "Случайный викинг ростом выше 180 см")
    public ResponseEntity<Viking> randomTallViking() {
        Optional<Viking> result = lambdaService.getRandomTallViking();
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/api/lambda/display/legendary")
    @Operation(summary = "Все викинги с легендарным снаряжением")
    public List<Viking> allLegendary() {
        return lambdaService.getAllWithLegendaryEquipment();
    }

    @GetMapping("/api/lambda/display/red-sorted")
    @Operation(summary = "Рыжебородые викинги, отсортированные по возрасту")
    public List<Viking> redBeardedSortedByAge() {
        return lambdaService.getRedBeardedSortedByAge();
    }

    @GetMapping("/api/lambda/ids/max")
    @Operation(summary = "Максимальный ID среди всех викингов")
    public ResponseEntity<Integer> maxId() {
        return lambdaService.findMaxId()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/api/lambda/ids/even")
    @Operation(summary = "Все чётные ID среди всех викингов на сервере")
    public List<Integer> evenIds() {
        return lambdaService.findEvenIds();
    }

    @PostMapping("/api/vikings/bulk")
    @Operation(summary = "Массовая генерация викингов через фабрику и лямбда-операции")
    public List<Viking> generateBulk(
            @Parameter(description = "Количество викингов для генерации", example = "10")
            @RequestParam(defaultValue = "10") int count) {
        return vikingService.generateBulkVikings(count);
    }
}