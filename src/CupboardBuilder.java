public interface CupboardBuilder {
    Cupboard build();
    CupboardBuilder height(double height);
    CupboardBuilder length(double length);
    CupboardBuilder width(double width);
    CupboardBuilder color (String color);
    CupboardBuilder numOfSections(int numOfSections);
}
