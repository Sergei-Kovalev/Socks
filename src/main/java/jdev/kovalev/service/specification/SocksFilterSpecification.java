package jdev.kovalev.service.specification;

import jdev.kovalev.entity.Socks;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SocksFilterSpecification {

    public static Specification<Socks> hasColor(String color) {
        return (root, query, criteriaBuilder) ->
                color == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("color"), color);
    }

    public static Specification<Socks> hasCottonMoreThan(Integer cottonMoreThan) {
        return (root, query, criteriaBuilder) ->
                cottonMoreThan == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.greaterThan(root.get("cottonPercentage"), cottonMoreThan / 100);
    }

    public static Specification<Socks> hasCottonLessThan(Integer cottonLessThan) {
        return (root, query, criteriaBuilder) ->
                cottonLessThan == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.lessThan(root.get("cottonPercentage"), cottonLessThan / 100);
    }

    public static Specification<Socks> hasCottonEqual(Integer cottonEqual) {
        return (root, query, criteriaBuilder) ->
                cottonEqual == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("cottonPercentage"), cottonEqual / 100);
    }

    public static Specification<Socks> hasNumberMoreThan(Integer numberMoreThan) {
        return (root, query, criteriaBuilder) ->
                numberMoreThan == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("number"), numberMoreThan);
    }

    public static Specification<Socks> hasNumberLessThan(Integer numberLessThan) {
        return (root, query, criteriaBuilder) ->
                numberLessThan == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("number"), numberLessThan);
    }

    public static Specification<Socks> hasNumberEqual(Integer numberEqual) {
        return (root, query, criteriaBuilder) ->
                numberEqual == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("number"), numberEqual);
    }
}
