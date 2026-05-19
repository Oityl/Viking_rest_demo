package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {

    private final VikingService vikingService;
    private VikingListener vikingListener;

    public VikingController(VikingService vikingService, VikingListener vikingListener) {
        this.vikingService = vikingService;
        this.vikingListener = vikingListener;
    }

    @GetMapping
    @Operation(summary = "Получить список созданных викингов", operationId = "getAllVikings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<Viking> getAllVikings() {
        System.out.println("GET /api/vikings called");
        return vikingService.findAll();
    }

    @GetMapping("/test")
    @Operation(summary = "Получить список тестовых викингов", operationId = "getTest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<String> test() {
        System.out.println("GET /api/vikings/test called");
        return List.of("Ragnar", "Bjorn");
    }

    @PostMapping("/post")
    @Operation(summary = "Создать случайного викинга", operationId = "addRandomViking")
    public void addViking() {
        vikingListener.testAdd();
    }

    @PostMapping
    @Operation(summary = "Добавить конкретного викинга", operationId = "createViking")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Викинг успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса")
    })
    public ResponseEntity<Viking> createViking(@RequestBody Viking viking) {
        Viking saved = vikingService.addViking(viking);
        vikingListener.notifyAdd(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{name}")
    @Operation(summary = "Перезаписать параметры конкретного викинга", operationId = "updateViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Параметры успешно обновлены"),
            @ApiResponse(responseCode = "404", description = "Викинг с таким именем не найден")
    })
    public ResponseEntity<Viking> updateViking(
            @Parameter(description = "Текущее имя викинга", example = "Ragnar")
            @PathVariable String name,
            @RequestBody Viking viking) {
        Optional<Viking> updated = vikingService.updateViking(name, viking);
        updated.ifPresent(v -> vikingListener.notifyUpdate(name, v));
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Удалить викинга", operationId = "deleteViking")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Викинг успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Викинг с таким именем не найден")
    })
    public ResponseEntity<Void> deleteViking(
            @Parameter(description = "Имя викинга для удаления", example = "Ragnar")
            @PathVariable String name) {
        boolean removed = vikingService.removeViking(name);
        if (removed) vikingListener.notifyRemove(name);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
