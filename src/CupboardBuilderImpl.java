public class CupboardBuilderImpl implements CupboardBuilder {
    private Cupboard cupboard;

    CupboardBuilderImpl() {
        cupboard = new Cupboard();
    }

    @Override
    public Cupboard build() {
        return cupboard;
    }

    @Override
    public CupboardBuilder height(double height) {
        cupboard.setHeight(height);
        return this;
    }

    @Override
    public CupboardBuilder length(double length) {
        cupboard.setLength(length);
        return this;
    }

    @Override
    public CupboardBuilder width(double width) {
        cupboard.setWidth(width);
        return this;
    }

    @Override
    public CupboardBuilder color(String color) {
        cupboard.setColor(color);
        return this;
    }

    @Override
    public CupboardBuilder numOfSections(int numOfSections) {
        cupboard.setNumOfSections(numOfSections);
        return this;
    }


}
