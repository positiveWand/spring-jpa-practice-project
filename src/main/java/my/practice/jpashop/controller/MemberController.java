package my.practice.jpashop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.practice.jpashop.domain.Address;
import my.practice.jpashop.domain.Member;
import my.practice.jpashop.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        // @Valid를 이용해 MemberForm에 있는 @NotNull validation 수행 -> 오류 발생

        // 오류 발생한 경우
        if(result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        // 엔티티를 그대로 사용하는 것은 바람직하지 않다
        // View 전용 객체에 맵핑하는 것이 바람직
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
