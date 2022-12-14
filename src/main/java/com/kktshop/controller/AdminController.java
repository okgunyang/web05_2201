package com.kktshop.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.JsonObject;
import com.kktshop.dto.GoodsDTO;
import com.kktshop.dto.MemberDTO;
import com.kktshop.dto.NavbarDTO;
import com.kktshop.dto.NoticeDTO;
import com.kktshop.dto.QnaDTO;
import com.kktshop.dto.ReviewDTO;
import com.kktshop.dto.SalesDTO;
import com.kktshop.dto.SearchDTO;
import com.kktshop.service.GoodsService;
import com.kktshop.service.HomeService;
import com.kktshop.service.MemberService;
import com.kktshop.service.NoticeService;
import com.kktshop.service.QnaService;
import com.kktshop.service.ReviewService;
import com.kktshop.service.SalesService;
import com.kktshop.util.Email2;
import com.kktshop.util.EmailSender2;
import com.kktshop.util.PageMaker;

@Controller
@RequestMapping("/admin/")
public class AdminController {

	@Autowired
	HomeService homeService;
	
	@Autowired
	MemberService memberService;

	@Autowired
	NoticeService noticeService;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	SalesService salesService;
	
	@Autowired
	ReviewService reviewService;
	
	@Autowired
	QnaService qnaService;	
	
	@Autowired
	BCryptPasswordEncoder passEncoder;
	
	@Autowired
	private EmailSender2 emailSender2;

	@Autowired
	private Email2 email2;
	
	@Autowired
	HttpSession session;
	
	//????????? ?????????
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String AdminRoot() {
        return "admin/admin";
    }
	
    //????????? ?????????
    @RequestMapping(value="admin.do", method = RequestMethod.GET)
    public String Admin() {
        return "admin/admin";
    }
    
	//?????? ?????? ????????????
	@RequestMapping(value="goodsList.do", method = RequestMethod.GET)
    public String goodsList(Model model) throws Exception {
		int cnt = goodsService.goodsCount();
		List<GoodsDTO> goodsList = goodsService.goodsList();
		model.addAttribute("cnt", cnt);
		model.addAttribute("cate_tit", "?????? ??????");
		model.addAttribute("goodsList", goodsList);
        return "admin/goodsList";
    }
	
	//ajax??? ???????????? ?????? ????????????
	@ResponseBody
	@RequestMapping(value="categoryLoading.do", method = RequestMethod.GET)
    public List<NavbarDTO> categoryLoading(String navparam, Model model) throws Exception {
		List<NavbarDTO> cateList = goodsService.cateList(navparam);
		return cateList;
    }
	
	//ajax??? ????????????????????? ?????? ?????? ?????? ????????????
	@ResponseBody
	@RequestMapping(value="navLoading.do", method = RequestMethod.POST)
    public List<NavbarDTO> navLoading(Model model) throws Exception {
		List<NavbarDTO> navList = homeService.navList();
		return navList;
    }
	
	//?????? ????????? ??????????????? ????????? ???????????? ????????? ?????? ?????? ????????????
	@RequestMapping(value="prolist.do", method = RequestMethod.GET)
    public String proList(@RequestParam("navparam") String navparam, Model model) throws Exception {
		List<GoodsDTO> goodsList = goodsService.proList(navparam);
		int cnt = goodsService.proCount(navparam);
		String navname = goodsService.navnameLoading(navparam);
		model.addAttribute("cnt", cnt);
		model.addAttribute("cate_tit", navname);
		model.addAttribute("goodsList", goodsList);
        return "admin/goodsList";
    }
	
	//?????? ????????????
	@RequestMapping(value="addGoods.do", method = RequestMethod.POST)
    public String addGoods(GoodsDTO goods, Model model) throws Exception {
		goodsService.addGoods(goods);
        return "redirect:/admin/goodsList.do";
    }
	
	//?????? ?????? ?????? ??????
	@RequestMapping(value="addGoodsForm.do", method = RequestMethod.GET)
    public String addGoodsForm(Model model) throws Exception {
		List<NavbarDTO> navbarList = homeService.navList();
		model.addAttribute("navbarList", navbarList);
        return "admin/addGoodsForm";
    }
	
	//?????? ?????? ????????????
	@RequestMapping(value="updateGoods.do", method = RequestMethod.POST)
    public String updateGoods(GoodsDTO goods, Model model) throws Exception {
		goodsService.updateGoods(goods);
        return "redirect:/admin/goodsList.do";
    }
	
	//?????? ?????? ????????????
	@RequestMapping(value="deleteGoods.do", method = RequestMethod.GET)
    public String deleteGoods(GoodsDTO goods, Model model) throws Exception {
		goodsService.deleteGoods(goods);
        return "redirect:/admin/goodsList.do";
    }
	
	//?????? ?????? ????????????
	@RequestMapping(value="getGoods.do", method = RequestMethod.GET)
    public String getGoods(int gnum, Model model) throws Exception {
		GoodsDTO goods = goodsService.getGoods(gnum);
		model.addAttribute("goods", goods);
        return "admin/getGoods";
    }
	
	//ajax??? ?????? ?????? ?????? ????????????
	@ResponseBody
	@RequestMapping(value="getAjaxGoods.do", method = RequestMethod.GET)
    public GoodsDTO getAjaxGoods(int gnum, Model model) throws Exception {
		GoodsDTO goods = goodsService.getGoods(gnum);
        return goods;
    }
	
	
	//??????????????? ????????? ?????? ??????
	@RequestMapping(value="goodsImgUploadForm.do", method = RequestMethod.GET)
    public String goodsImgUploadForm(Model model) throws Exception {
		List<NavbarDTO> navbarList = homeService.navList();
		model.addAttribute("navbarList", navbarList);
        return "admin/goodsImgUploadForm";
    }
	
	//??????????????? ?????????
	@RequestMapping(value="goodsImgUploadPro.do", method = RequestMethod.POST)
    public String goodsImgUploadPro(MultipartFile myfile, Model model) throws UnsupportedEncodingException, Exception {
		String uploadFolder = "D:\\kim3\\jsp3\\web05\\src\\main\\webapp\\WEB-INF\\views\\upload"; //????????????
		//String uploadFolder = "/WEB-INF/views/upload"; //????????????
		String fileName = myfile.getOriginalFilename();
		File saveFile = new File(uploadFolder, fileName);
		myfile.transferTo(saveFile);
    	model.addAttribute("filename", fileName);
    	return "admin/goodsImgUploadForm";
    }
	
	//?????????(????????? ??????) ????????????
	@RequestMapping(value="newGoodsLoading.do", method = RequestMethod.GET)
	public String newGoodsLoading(Model model) throws Exception {
		int cnt = goodsService.goodsCount();
		List<GoodsDTO> goodsList = homeService.latestGoods();
		model.addAttribute("cnt", cnt);
		model.addAttribute("cate_tit", "?????????");
		model.addAttribute("goodsList",goodsList);
		return "admin/goodsList";
	}	
        
    //???????????? ?????? ???
    @RequestMapping(value="cateForm.do", method = RequestMethod.GET)
    public String cateForm(Model model) throws Exception {
    	List<NavbarDTO> navbarList = homeService.navList();
    	model.addAttribute("navbarList",navbarList);
        return "admin/cateForm";
    }
    
    //?????? ?????? ?????? ?????? ??????
    @ResponseBody
    @RequestMapping(value="checkNav.do", method = RequestMethod.GET)
    public String checkNav(@RequestParam("navparam") String navparam) throws Exception {
    	String data = "";
    	int cnt = homeService.navCount(navparam); 
    	if(cnt >= 1) {
    		data = "no";
    	} else {
    		data = "ok";
    	}
    	return data;
    }
    
    //???????????? ?????? ??????
    @RequestMapping(value="addCate.do", method = RequestMethod.POST)
    public String addCate(NavbarDTO navbar, Model model) throws Exception {
    	homeService.addCate(navbar);
    	return "redirect:/admin/admin.do";
    }
    
    //???????????? ?????? ??????
    @RequestMapping(value="delCate.do", method = RequestMethod.GET)
    public String delCate(@RequestParam("idx") int idx, Model model) throws Exception {
    	homeService.delCate(idx);
    	return "redirect:/admin/admin.do";
    }
    
    //?????? ??????
	@RequestMapping(value="memberList.do", method = RequestMethod.GET)
    public String memberList(Model model) throws Exception {
		int cnt = memberService.memberCount();
		List<MemberDTO> memberList = memberService.memberList();
		model.addAttribute("cnt", cnt);
		model.addAttribute("memberList", memberList);
        return "admin/memberList";
    }
    
	//?????? ?????? ??????
	@RequestMapping(value="getMember.do", method = RequestMethod.GET)
    public String getMember(@RequestParam("id") String id, Model model) throws Exception {
		MemberDTO member = memberService.getMember(id);
		model.addAttribute("member", member);
        return "admin/getMember";
    }

    //?????? ???????????? ??? ????????????
    @RequestMapping(value="joinMemberForm.do", method = RequestMethod.GET)
	public String joinMemberForn(MemberDTO member, Model model) throws Exception {
		return "admin/joinForm";
	}
	
    //?????? ????????????
    @RequestMapping(value="addMember.do", method = RequestMethod.POST)
	public String addMember(MemberDTO member, Model model) throws Exception {
    	String reqpass = member.getPwd();
    	String pwd = passEncoder.encode(reqpass);
    	member.setPwd(pwd);
    	memberService.joinMember(member);
		return "admin/memberList";
	}
	
	//?????? ?????? ????????????
    @RequestMapping(value="deleteMember.do", method = RequestMethod.GET)
    public String deleteMember(@RequestParam("id") String id, MemberDTO member) throws Exception {
    	member.setId(id);
		memberService.deleteMember(member);
		return "admin/memberList";
    }
    
    //?????? ???????????? ??????
    @RequestMapping(value="updateMember.do", method = RequestMethod.POST)
	public String updateMember(MemberDTO member) throws Exception {
    	sendEmailAction(member);	//?????? (????????????)?????? ????????? ?????????
    	String reqpass = member.getPwd();
    	String pwd = passEncoder.encode(reqpass);
    	member.setPwd(pwd);
    	memberService.updateMember(member);
		return "admin/memberList";
	}
    
    //???????????? ?????? ????????? ?????????
    public void sendEmailAction(MemberDTO member) throws Exception {
    	String id = member.getId();
    	String e_mail = member.getEmail();
    	String pw = member.getPwd();
    	System.out.println(pw);
    	if(pw!=null) {
    		email2.setContent("??????????????? "+pw+" ?????????.");
    		email2.setReceiver(e_mail);
    		email2.setSubject(id+"??? ???????????? ?????? ??????????????????.");
    		emailSender2.SendEmail(email2);
    	}
    }

    //???????????? ?????? ??? ??????
    @RequestMapping(value="adminNoticeForm.do", method = RequestMethod.GET)
    public String adminNoticeForm() {
    	return "admin/adminNoticeForm";
    }
    

	//DataTables jQuery ??????????????? ????????? ??????
	@RequestMapping(value="noticeList.do", method = RequestMethod.GET)
    public String noticeList(Model model) throws Exception {
		List<NoticeDTO> noticeList = noticeService.noticeList();
		int cnt = noticeService.noticeCount();
		model.addAttribute("noticeList", noticeList);
		model.addAttribute("cnt", cnt);
        return "admin/noticeList";
    }
	
	//????????????????????? ????????? ????????? ???????????? ??????
	@RequestMapping(value="pageList.do", method = RequestMethod.GET)
    public String noticePageList(@RequestParam("curPage") int curPage, Model model) throws Exception {
		int cnt = noticeService.noticeCount();
		//PageMaker page = new PageMaker(cnt, curPage);
		//PageMaker page = new PageMaker(cnt, curPage, 10);
		PageMaker page = new PageMaker(cnt, curPage, 10, 10);
		List<NoticeDTO> noticePageList = noticeService.noticePageList(page);
		model.addAttribute("noticePageList", noticePageList);
		model.addAttribute("page", page);
		model.addAttribute("cnt", cnt);
        return "admin/noticePageList";
    }
	
	//?????? ????????? ?????? ?????? 
	@RequestMapping(value="addNoticeForm.do", method = RequestMethod.GET)
    public String addNoticeForm(Model model) throws Exception {
        return "admin/addNoticeForm";
    }
	
	//?????? ????????? ??????
	@RequestMapping(value="addNotice.do", method = RequestMethod.POST)
    public String addNotice(NoticeDTO notice, Model model) throws Exception {
		noticeService.addNotice(notice);
        return "redirect:/admin/noticeList.do";
    }
	
	/* ckeditor ??? ?????? */ 
	@RequestMapping(value="addSmartNoticeForm.do", method = RequestMethod.GET)
    public String addNoticeSmartForm(Model model) throws Exception {
        return "admin/addSmartNoticeForm";
    }
	
	/* ckeditor??? ????????? ????????? ?????? */
	@RequestMapping(value="addSmartNotice.do", method = RequestMethod.POST)
    public String addSmartNotice(NoticeDTO notice, Model model) throws Exception {
		noticeService.addNotice(notice);
        return "redirect:/admin/pageList.do";
    }
	
	//?????? ?????????
	@ResponseBody
	@RequestMapping(value = "fileupload.do")
    public void communityImageUpload(HttpServletRequest req, HttpServletResponse resp, MultipartHttpServletRequest multiFile) throws Exception{
		JsonObject jsonObject = new JsonObject();
		PrintWriter printWriter = null;
		OutputStream out = null;
		MultipartFile file = multiFile.getFile("upload");
		
		if(file != null) {
			if(file.getSize() >0 && StringUtils.isNotBlank(file.getName())) {
				if(file.getContentType().toLowerCase().startsWith("image/")) {
				    try{
				    	 
			            String fileName = file.getOriginalFilename();
			            byte[] bytes = file.getBytes();
			           
			            //String uploadPath = req.getSession().getServletContext().getRealPath("/views/upload"); //????????????
			            String uploadPath = "D:\\kim3\\jsp3\\web05\\src\\main\\webapp\\WEB-INF\\views\\upload";
			            System.out.println("uploadPath:"+uploadPath);

			            File uploadFile = new File(uploadPath);
			            if(!uploadFile.exists()) {
			            	uploadFile.mkdir();
			            }
			            String fileName2 = UUID.randomUUID().toString();
			            uploadPath = uploadPath + "/" + fileName2 +fileName;
			            
			            out = new FileOutputStream(new File(uploadPath));
			            out.write(bytes);
			            
			            printWriter = resp.getWriter();
			            //String fileUrl = "/views/upload/"+fileName2+fileName;
			            String fileUrl = "D:\\kim3\\jsp3\\web05\\src\\main\\webapp\\WEB-INF\\views\\upload"+fileName2+fileName; //url??????
			            System.out.println("fileUrl :" + fileUrl);
			            JsonObject json = new JsonObject();
			            json.addProperty("uploaded", 1);
			            json.addProperty("fileName", fileName);
			            json.addProperty("url", fileUrl);
			            printWriter.print(json);
			            System.out.println(json);
			 
			        }catch(IOException e){
			            e.printStackTrace();
			        } finally {
			            if (out != null) {
		                    out.close();
		                }
		                if (printWriter != null) {
		                    printWriter.close();
		                }
			        }
				}
			}
		}
	}
	
	@RequestMapping(value="noticeRead.do", method = RequestMethod.GET)
    public String noticeRead(@RequestParam("seq") int seq, Model model) throws Exception {
		NoticeDTO notice = noticeService.noticeRead(seq);
		model.addAttribute("notice", notice);
        return "admin/noticeRead";
    }
	
	@RequestMapping(value="updateNotice.do", method = RequestMethod.POST)
    public String updateNotice(NoticeDTO notice, Model model) throws Exception {
		noticeService.updateNotice(notice);
		return "redirect:/admin/noticeList.do";
    }
	
	@RequestMapping(value="deleteNotice.do", method = RequestMethod.GET)
    public String deleteNotice(@RequestParam("seq") int seq, Model model) throws Exception {
		NoticeDTO notice = new NoticeDTO();
		notice.setSeq(seq);
		noticeService.deleteNotice(notice);
		return "redirect:/admin/noticeList.do";
    }
    
	//???????????? ????????? ????????? ????????? a????????? ?????? GET?????? ??????????????? ???
	@RequestMapping(value="qnaList.do", method = RequestMethod.GET)
	public String qnaBasicList(Model model) throws Exception {
		List<QnaDTO> qnaList = qnaService.qnaList();
		model.addAttribute("qnaList", qnaList);
		return "admin/qnaList";
	}
	
	//?????? ???(Form)?????? ?????? ????????? ???????????? ???????????? ???????????? POST ??????
	@RequestMapping(value="qnaList.do", method = RequestMethod.POST)
	public String qnaList(SearchDTO sdto, Model model) throws Exception {
		List<QnaDTO> qnaList = qnaService.qnaList(sdto);
		model.addAttribute("qnaList", qnaList);
		return "admin/qnaList";
	}

	@RequestMapping(value="qnaRead.do", method = RequestMethod.GET)
	public String qnaRead(@RequestParam int qno, Model model) throws Exception {
		QnaDTO qna = qnaService.qnaRead(qno);
		model.addAttribute("qna", qna);
		return "admin/qnaRead";
	}

	@RequestMapping(value="replyRead.do", method = RequestMethod.GET)
	public String replyRead(@RequestParam int qno, Model model) throws Exception {
		QnaDTO qna = qnaService.replyRead(qno);
		model.addAttribute("qna", qna);
		return "admin/replyRead";
	}
	
	@RequestMapping("qnaWriteForm.do")  
	public String qnaWriteForm(Model model) throws Exception {
		return "admin/qnaWriteForm";
	}
	
	@RequestMapping(value="replyForm.do", method=RequestMethod.GET)  
	public String replyWriteForm(@RequestParam int qno, Model model) throws Exception {
		QnaDTO qna = qnaService.qnaRead(qno);
		model.addAttribute("qna", qna);
		return "admin/replyWriteForm";
	}
	
	@RequestMapping(value="qnaInsert.do", method = RequestMethod.POST)
	public String qnaWrite(QnaDTO qdto, Model model) throws Exception {
		qnaService.qnaWrite(qdto);
		return "redirect:/admin/qnaList.do";
	}

	@RequestMapping(value="replyInsert.do", method = RequestMethod.POST)
	public String replyWrite(QnaDTO qdto, Model model) throws Exception {
		qnaService.replyWrite(qdto);
		return "redirect:/admin/qnaList.do";
	}
	
	@RequestMapping(value="qnaUpdate.do", method = RequestMethod.POST)
	public String qnaUpdate(QnaDTO qdto, Model model) throws Exception {
		qnaService.qnaUpdate(qdto);
		return "redirect:/admin/qnaList.do";
	}

	@RequestMapping(value="qnaDelete.do", method = RequestMethod.GET)
	public String qnaDelete(@RequestParam int qno, Model model) throws Exception {
		qnaService.qnaDelete(qno);
		return "redirect:/admin/qnaList.do";
	}
	
	@RequestMapping(value="replyDelete.do", method = RequestMethod.GET)
	public String replyDelete(@RequestParam int qno, Model model) throws Exception {
		qnaService.replyDelete(qno);
		return "redirect:/admin/qnaList.do";
	}
    
    //?????? ??????
	//?????? ?????? ??????
	@RequestMapping(value="salesList.do", method = RequestMethod.GET)
    public String salesList(Model model) throws Exception {
		List<SalesDTO> salesList = salesService.salesList();
		model.addAttribute("salesList", salesList);
        return "admin/salesList";
    }
    
    //????????? ??????
	//???????????? ?????? ??????
	@RequestMapping(value="addShuttleForm.do", method = RequestMethod.GET)
	public String addShuttleForm(int ono, Model model) throws Exception {
		SalesDTO sale = salesService.salesRead(ono);
		model.addAttribute("sales", sale);
		return "admin/addShuttleForm";
	}

	//????????? ????????? ?????? ??????
	@RequestMapping(value="addShuttle.do", method = RequestMethod.POST)
	public String addShuttle(SalesDTO sales, Model model) throws Exception {
		System.out.println("???????????? : "+sales.getTransno());
		System.out.println("????????? : "+sales.getTransco());
		salesService.addShipping(sales);
		return "admin/admin";
	}
	
	//DataTables jQuery ??????????????? ????????? ??????
	@RequestMapping(value="reviewList.do", method = RequestMethod.GET)
    public String reviewList(Model model) throws Exception {
		List<ReviewDTO> reviewList = reviewService.reviewAllList();
		int cnt = reviewService.reviewCount();
		model.addAttribute("reviewList", reviewList);
		model.addAttribute("cnt", cnt);
        return "admin/reviewList";
    }
}