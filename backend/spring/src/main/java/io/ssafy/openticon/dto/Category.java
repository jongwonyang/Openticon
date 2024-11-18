package io.ssafy.openticon.dto;

public enum Category {

    REAL("실사"), CHARACTER("캐릭터"), ENTERTAINMENT("방송&연예"), TEXT("글자");

    final private String name;

    private Category(String name){
        this.name=name;
    }

    public String getName(){
        return this.name;
    }

    public static Category nameOf(String name){
        for(Category category: Category.values()){
            if(category.getName().equals(name)){
                return category;
            }
        }
        return null;
    }
}
