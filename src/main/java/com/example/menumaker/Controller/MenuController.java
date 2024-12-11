package com.example.menumaker.Controller;

import com.example.menumaker.model.MenuItem;
import com.example.menumaker.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class MenuController {

    @Autowired
    private MenuItemRepository menuItemRepository;

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);


    @GetMapping("/menu/Salkara")
    public String menuSalkara(Model model) {
        int shopId = 15;
        List<MenuItem> menuList = menuItemRepository.findAllByShopId(shopId);
        Map<String, List<MenuItem>> categorizedMenu = menuList.stream()
                .collect(Collectors.groupingBy(menuItem -> menuItem.getShopMenuCategory().getName()));

        model.addAttribute("categorizedMenu", categorizedMenu);
        logger.info("Categorized Menu: {}", categorizedMenu);
        return "menu/index";
    }

}
