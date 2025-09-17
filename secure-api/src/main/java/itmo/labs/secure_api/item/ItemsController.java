package itmo.labs.secure_api.item;

import static itmo.labs.secure_api.util.Sanitizer.forHtml;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemsController {

    private final ItemRepository itemRepository;

    public ItemsController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public record ItemCreateRequest(@NotBlank @Size(max = 255) String title) {
    }

    public record ItemResponse(Long id, String title) {
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> create(@RequestBody @Valid ItemCreateRequest body, Authentication auth) {
        Item item = new Item();
        item.setTitle(body.title());
        Item saved = itemRepository.save(item);
        return Map.of(
                "createdBy", auth.getName(),
                "item", new ItemResponse(saved.getId(), forHtml(saved.getTitle())));
    }

    @GetMapping
    public List<ItemResponse> list() {
        return itemRepository.findAll().stream()
                .map(i -> new ItemResponse(i.getId(), forHtml(i.getTitle())))
                .toList();
    }
}
