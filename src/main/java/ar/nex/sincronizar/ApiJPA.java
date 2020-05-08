package ar.nex.sincronizar;

import ar.nex.entity.Item;
import ar.nex.jpa.service.JpaService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

/**
 *
 * @author Renzo
 */
public class ApiJPA {

    private EntityManager em;

    public ApiJPA() {
        this.em = new JpaService().getFactory().createEntityManager();
    }

    public <T> Long getCountOfEntity(Class<T> claz) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> studentQuery = builder.createQuery(Long.class);
        Root<T> root = studentQuery.from(claz);
        Expression<Long> countExpression = builder.count(root);
        studentQuery.select(countExpression);
        TypedQuery<Long> typedStudentQuery = em.createQuery(studentQuery);

        return typedStudentQuery.getSingleResult();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ApiJPA api = new ApiJPA();

        Item item = new JpaService().getItem().findItem("08a4bd63-7977-4409-950f-5997fb41662b");
        System.out.println("ar.nex.sincronizar.ApiJPA.main()" + item.toString());

        Long lg = api.getCountOfEntity(Item.class);
        System.out.println("ar.nex.sincronizar.ApiJPA.main() " + lg);
        
        List<Item> lst = new JpaService().getItem().findItemEntities();
        lst.stream().findFirst().ifPresent(System.out::println);
    }

}
