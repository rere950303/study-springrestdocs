package docs.demo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TestController {

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable String id) {
        return Account.builder().id(Long.valueOf(id)).name("account " + id).email("email " + id).build();
    }

    @GetMapping("/accounts")
    public ResponseDTO getAccountList(@RequestParam Integer size, @RequestParam Integer page) {
        List<Account> list = List.of(Account.builder().id(Long.valueOf(1)).name("account " + 1).email("email " + 1).build(),
                Account.builder().id(Long.valueOf(2)).name("account " + 2).email("email " + 2).build());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.data.put("list", list);

        return responseDTO;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createAccount(@ModelAttribute AccountDTO accountDTO) {
        return 1L;
    }

    @GetMapping("/favicon.ico")
    public void favicon() {

    }

}