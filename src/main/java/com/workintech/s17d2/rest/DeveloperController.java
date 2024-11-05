package com.workintech.s17d2.rest;

import com.workintech.s17d2.dto.DeveloperResponse;
import com.workintech.s17d2.model.DevFactory;
import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.model.JuniorDeveloper;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

   public Map<Integer, Developer> developers;
   private Taxable taxable;

    @Autowired
    public DeveloperController(@Qualifier("developerTax") Taxable taxable){
        this.taxable = taxable;
    }

    @PostConstruct
    public void init(){
        this.developers = new HashMap<>();
        this.developers.put(1,new JuniorDeveloper(1,"Rauf",500d));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeveloperResponse save(@RequestBody Developer developer){
        Developer createdDeveloper = DevFactory.createDeveloper(developer,taxable);
        if (Objects.nonNull(createdDeveloper)){
            developers.put(createdDeveloper.getId(),createdDeveloper);
        }

        return new DeveloperResponse (createdDeveloper,HttpStatus.CREATED.value(),"Dev Created");
    }

    @GetMapping
    public List<Developer> getAll(){
        return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public DeveloperResponse getById(@PathVariable("id") int id){
        Developer foundDev = this.developers.get(id);
        if (foundDev == null){
            return new DeveloperResponse(null,HttpStatus.NOT_FOUND.value(), "bulamadık");
        }
        return new DeveloperResponse(foundDev,HttpStatus.OK.value(), "Başarılı");
    }

    @PutMapping("/{id}")
    public DeveloperResponse update(@PathVariable("id") int id, @RequestBody Developer developer){
        developer.setId(id);
        Developer newDev = DevFactory.createDeveloper(developer,taxable);
        this.developers.put(id,newDev);
        return new DeveloperResponse(newDev,HttpStatus.OK.value(), "Başarılı");
    }

    @DeleteMapping("/{id}")
    public DeveloperResponse delete(@PathVariable("id")int id){
       Developer removedDev =  this.developers.get(id);
        this.developers.remove(id);
        return new DeveloperResponse(removedDev,HttpStatus.NO_CONTENT.value(), "Silindi");
    }


}
