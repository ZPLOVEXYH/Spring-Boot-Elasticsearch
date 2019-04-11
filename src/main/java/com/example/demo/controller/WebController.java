package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.Poem;
import com.example.demo.entity.VueJs;
import com.example.demo.repository.VueJsRepository;
import com.example.demo.service.PoemServiceImpl;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by linziyu on 2018/5/19. 控制层
 *
 */

@Controller
public class WebController
{
	@Autowired
	private PoemServiceImpl poemService;

	Logger logger =  LoggerFactory.getLogger(WebController.class);

	@RequestMapping("/")
	public String index()
	{
		List<Poem> poems = new ArrayList<>();
		poems.add(new Poem(4, "湘春夜月·近清明", "近清明,翠禽枝上消魂,可惜一片清歌，都付与黄昏。欲共柳花低诉，怕柳花轻薄，不解伤春。念楚乡旅宿，柔情别绪，谁与温存。"));
		poems.add(new Poem(5, "卜算子·不是爱风尘", "不是爱风尘，似被前缘误。花落花开自有时，总赖东君主。\n" + "去也终须去，住也如何住！若得山花插满头，莫问奴归处"));
		poems.add(new Poem(6, "御街行·秋日怀旧", "纷纷坠叶飘香砌。夜寂静，寒声碎。真珠帘卷玉楼空，天淡银河垂地。年年今夜，月华如练，长是人千里。"));

		logger.info("elasticsearch ======" + poems);
		for (int i = 0; i < poems.size(); i++)
		{
			poemService.save(poems.get(i));
		}

		return "/index";

	}

	@RequestMapping("/tt")
	public String index1(@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize, Model model)
	{
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		Page<Poem> poems = poemService.findAll(pageable);
		List<Poem> poems1 = poems.getContent();
		model.addAttribute("poems", poems);
		return "/index";
	}

	@RequestMapping("/t")
	public String index2(@RequestParam(value = "content", required = false, defaultValue = "香") String content,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize, Model model)
	{
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		Page<Poem> poems = poemService.search(content, pageable);
		List<Poem> list = poems.getContent();
		model.addAttribute("poems", list);
		return "/t";
	}

	@RequestMapping("/search")
	public String search(String content,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize, Model model)
	{
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		Page<Poem> poems = poemService.search(content, pageable);
		List<Poem> list = poems.getContent();
		model.addAttribute("poems", list);
		return "/list";

	}

	@RequestMapping("/delete")
	public @ResponseBody String deletePormByContent()
    {
        Pageable pageable = new PageRequest(0, 10);
        Page<Poem> poems = poemService.search("香", pageable);
        for (Poem p : poems)
        {
            poemService.delete(p);
        }
        return "SUCCESS";
    }

    /**
     * 更新elasticsearch仓库里面的数据
     *
     * @return
     */
    @RequestMapping("/update")
    public @ResponseBody String update()
    {
        Pageable pageable = new PageRequest(0, 10);
        Page<Poem> poems = poemService.search("清歌", pageable);
        for (Poem p : poems)
        {
            p.setTitle("一首好诗");
            poemService.save(p);
        }

        return "SUCCESS";
    }

    @Autowired
	VueJsRepository vueJsRepository;

    /**
     * 更新elasticsearch仓库里面的数据
     *
     * @return
     */
    @RequestMapping("/test-vue")
    public @ResponseBody List<Object> testVue(String message,
										@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
										@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize)
    {
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		Page<VueJs> vueJs = vueJsRepository.findByMessageLike(message, pageable);
		List<Object> messageList = vueJs.stream().map(x -> {
			String msg = x.getMessage().replace("/\"", "\"");
			int startIdx = msg.indexOf("{");

			return JSONArray.parseObject(msg.substring(startIdx, msg.length()));
		}).collect(Collectors.toList());


        return messageList;
    }

	public static void main(String[] args) {
		String message = "vue-log,前端请求过来的数据vue.js logs{\\\"fullPath\\\":\\\"/admin/warehouse/international\\\",\\\"feature\\\":\\\"KABUTAKE_ROUTER_DONE\\\",\\\"title\\\":\\\"访问的页面在那美克星上\\\",\\\"ua\\\":\\\"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:66.0) Gecko/20100101 Firefox/66.0\\\",\\\"user\\\":\\\"admin\\\"}";
		int i = message.indexOf("{");
		System.out.println(message.substring(i, message.length()));
	}

}
