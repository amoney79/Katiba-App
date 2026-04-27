package app;

import backend.DatabaseManager;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.List;

/**
 * Run this ONCE to seed your MongoDB Atlas 'katiba' database
 * with the Kenya Constitution 2010 (chapters + sample articles).
 *
 * Usage:  mvn exec:java -Dexec.mainClass="app.DataSeeder"
 */
public class DataSeeder {

    public static void main(String[] args) {
        System.out.println("═══ Katiba Data Seeder ═══");
        MongoDatabase db = DatabaseManager.getInstance().getDatabase();
        MongoCollection<Document> articles = db.getCollection("articles");

        if (articles.countDocuments() > 0) {
            System.out.println("Collection already has " + articles.countDocuments() + " documents. Skipping.");
            DatabaseManager.getInstance().close();
            return;
        }

        // ── Create text index ──────────────────────────────────────────────────
        articles.createIndex(new Document("title", "text").append("content", "text"));

        // ── Seed data ─────────────────────────────────────────────────────────
        List<Document> seed = List.of(

            // ── Chapter 1: Sovereignty of the People ─────────────────────────
            art(1, "Sovereignty of the People and Supremacy of the Constitution",
                1, "Sovereignty of the People",
                "All sovereign power belongs to the people of Kenya and shall be exercised only in accordance with this Constitution.",
                List.of("sovereignty", "people", "power")),
            art(1, "Sovereignty of the People and Supremacy of the Constitution",
                2, "Supremacy of this Constitution",
                "This Constitution is the supreme law of the Republic and binds all persons and all State organs at both levels of government. No person may claim or exercise State authority except as authorised under this Constitution.",
                List.of("supremacy", "constitution", "law")),

            // ── Chapter 2: The Republic ───────────────────────────────────────
            art(2, "The Republic",
                3, "Declaration of the Republic",
                "Kenya is a sovereign Republic.",
                List.of("republic", "sovereign")),
            art(2, "The Republic",
                4, "Territory of Kenya",
                "The territory of Kenya consists of the area, including the territorial sea, inland waters, and the continental shelf, that was the territory of Kenya on the effective date, together with any additional area as defined by an Act of Parliament.",
                List.of("territory", "borders")),
            art(2, "The Republic",
                5, "Languages",
                "The national language of the Republic is Kiswahili. The official languages of the Republic are Kiswahili and English.",
                List.of("language", "kiswahili", "english")),
            art(2, "The Republic",
                6, "Devolution and Access to Services",
                "The Republic is divided into the counties specified in the First Schedule. The governments at the national and county levels are distinct and inter-dependent and shall conduct their mutual relations on the basis of consultation and cooperation.",
                List.of("devolution", "counties", "government")),
            art(2, "The Republic",
                7, "National, Official and Other Languages",
                "The State shall promote and protect the diversity of language of the people of Kenya.",
                List.of("language", "diversity", "culture")),

            // ── Chapter 3: Citizenship ────────────────────────────────────────
            art(3, "Citizenship",
                12, "Citizenship and its Benefits",
                "Every citizen is entitled to the rights, privileges and benefits of citizenship, subject to the limits provided or permitted by this Constitution.",
                List.of("citizenship", "rights", "privileges")),
            art(3, "Citizenship",
                13, "Citizenship by Birth",
                "A person is a citizen by birth if on the day of the person's birth, whether or not the person is born in Kenya, either the mother or father of the person is a citizen.",
                List.of("citizenship", "birth", "nationality")),
            art(3, "Citizenship",
                14, "Citizenship by Registration",
                "A person who has been married to a citizen for a period of at least seven years is entitled on application to be registered as a citizen.",
                List.of("citizenship", "marriage", "registration")),

            // ── Chapter 4: Bill of Rights ────────────────────────────────────
            art(4, "The Bill of Rights",
                19, "Rights and Fundamental Freedoms",
                "The Bill of Rights is an integral part of Kenya's democratic state and is the framework for social, economic and cultural policies. The purpose of recognising and protecting human rights and fundamental freedoms is to preserve the dignity of individuals and communities and to promote social justice and the realisation of the potential of all human beings.",
                List.of("rights", "freedoms", "dignity", "bill of rights")),
            art(4, "The Bill of Rights",
                20, "Application of Bill of Rights",
                "The Bill of Rights applies to all law and binds all State organs and all persons. Every person shall enjoy the rights and fundamental freedoms in the Bill of Rights to the greatest extent consistent with the nature of the right or fundamental freedom.",
                List.of("rights", "application", "state")),
            art(4, "The Bill of Rights",
                21, "Implementation of Rights and Fundamental Freedoms",
                "It is a fundamental duty of the State and every State organ to observe, respect, protect, promote and fulfil the rights and fundamental freedoms in the Bill of Rights.",
                List.of("rights", "state duty", "implementation")),
            art(4, "The Bill of Rights",
                26, "Right to Life",
                "Every person has the right to life. The life of a person begins at conception. A person shall not be deprived of life intentionally, except to the extent authorised by this Constitution or other written law.",
                List.of("life", "right to life")),
            art(4, "The Bill of Rights",
                27, "Equality and Freedom from Discrimination",
                "Every person is equal before the law and has the right to equal protection and equal benefit of the law. Equality includes the full and equal enjoyment of all rights and fundamental freedoms.",
                List.of("equality", "discrimination", "equal rights")),
            art(4, "The Bill of Rights",
                28, "Human Dignity",
                "Every person has inherent dignity and the right to have that dignity respected and protected.",
                List.of("dignity", "human rights")),
            art(4, "The Bill of Rights",
                29, "Freedom and Security of Person",
                "Every person has the right to freedom and security of the person. No person shall be denied personal liberty and security.",
                List.of("freedom", "security", "liberty")),
            art(4, "The Bill of Rights",
                31, "Privacy",
                "Every person has the right to privacy, which includes the right not to have their person, home or property searched, their possessions seized, information relating to their family or private affairs unnecessarily required or revealed.",
                List.of("privacy", "rights", "security")),
            art(4, "The Bill of Rights",
                33, "Freedom of Expression",
                "Every person has the right to freedom of expression, which includes freedom to seek, receive or impart information or ideas; freedom of artistic creativity; academic freedom and freedom of scientific research.",
                List.of("freedom", "expression", "speech", "media")),
            art(4, "The Bill of Rights",
                34, "Freedom and Independence of the Media",
                "Freedom and independence of electronic, print and all other types of media is guaranteed, but does not extend to any expression specified in Article 33(2).",
                List.of("media", "press freedom", "independence")),
            art(4, "The Bill of Rights",
                35, "Access to Information",
                "Every citizen has the right of access to information held by the State; and information held by another person and required for the exercise or protection of any right or fundamental freedom.",
                List.of("information", "access", "transparency")),
            art(4, "The Bill of Rights",
                37, "Assembly, Demonstration, Picketing and Petition",
                "Every person has the right, peaceably and unarmed, to assemble, to demonstrate, to picket, and to present petitions to public authorities.",
                List.of("assembly", "demonstration", "petition", "freedom")),
            art(4, "The Bill of Rights",
                38, "Political Rights",
                "Every citizen is free to make political choices, which includes the right to form, or participate in forming, a political party; to participate in the activities of, or recruit members for, a political party; and to campaign for a political party or cause.",
                List.of("political rights", "voting", "democracy")),
            art(4, "The Bill of Rights",
                39, "Freedom of Movement and Residence",
                "Every person has the right to freedom of movement; to leave Kenya; to enter, remain in and reside anywhere in Kenya; and to a passport or other travel document.",
                List.of("movement", "freedom", "travel", "passport")),
            art(4, "The Bill of Rights",
                40, "Protection of Right to Property",
                "Subject to Article 65, every person has the right, either individually or in association with others, to acquire and own property—of any description and in any part of Kenya.",
                List.of("property", "ownership", "rights")),
            art(4, "The Bill of Rights",
                43, "Economic and Social Rights",
                "Every person has the right to the highest attainable standard of health, which includes the right to health care services, including reproductive health care; to accessible and adequate housing, and to reasonable standards of sanitation; to be free from hunger, and to have adequate food of acceptable quality; to clean and safe water in adequate quantities; to social security; and to education.",
                List.of("health", "housing", "food", "water", "education", "social rights")),
            art(4, "The Bill of Rights",
                47, "Fair Administrative Action",
                "Every person has the right to administrative action that is expeditious, efficient, lawful, reasonable and procedurally fair.",
                List.of("administration", "fair action", "justice")),
            art(4, "The Bill of Rights",
                48, "Access to Justice",
                "The State shall ensure access to justice for all persons and, if any fee is required, it shall be reasonable and shall not impede access to justice.",
                List.of("justice", "access", "courts")),
            art(4, "The Bill of Rights",
                50, "Fair Hearing",
                "Every person has the right to have any dispute that can be resolved by the application of law decided in a fair and public hearing before a court or, if appropriate, another independent and impartial tribunal or body.",
                List.of("fair hearing", "trial", "courts", "justice")),

            // ── Chapter 5: Land and Environment ──────────────────────────────
            art(5, "Land and Environment",
                60, "Principles of Land Policy",
                "Land in Kenya shall be held, used and managed in a manner that is equitable, efficient, productive and sustainable, and in accordance with the following principles: equitable access to land; security of land rights; sustainable and productive management of land resources; transparent and cost effective administration of land.",
                List.of("land", "property", "policy")),
            art(5, "Land and Environment",
                69, "Obligations in Respect of the Environment",
                "The State shall ensure sustainable exploitation, utilisation, management and conservation of the environment and natural resources, and ensure the equitable sharing of the accruing benefits.",
                List.of("environment", "conservation", "sustainability")),
            art(5, "Land and Environment",
                70, "Enforcement of Environmental Rights",
                "If a person alleges that a right to a clean and healthy environment recognised and protected under Article 42 has been, is being or is likely to be, denied, violated, infringed or threatened, the person may apply to a court for redress.",
                List.of("environment", "rights", "enforcement")),

            // ── Chapter 6: Leadership and Integrity ──────────────────────────
            art(6, "Leadership and Integrity",
                73, "Responsibilities of Leadership",
                "Authority assigned to a State officer is a public trust to be exercised in a manner that is consistent with the purposes and objects of this Constitution; demonstrates respect for the people; brings honour to the nation and dignity to the office; and promotes public confidence in the integrity of the office.",
                List.of("leadership", "integrity", "public trust")),
            art(6, "Leadership and Integrity",
                75, "Conduct of State Officers",
                "A State officer shall behave, whether or not in the performance of official duties, in a manner that avoids any conflict between personal interests and public or official duties; does not compromise the integrity of the office of the State officer or of any State organ; and does not demean the office.",
                List.of("conduct", "state officers", "ethics")),

            // ── Chapter 7: Representation of the People ──────────────────────
            art(7, "Representation of the People",
                81, "General Principles for the Electoral System",
                "The electoral system shall comply with the following principles: freedom of citizens to exercise their political rights under Article 38; not more than two-thirds of the members of elective public bodies shall be of the same gender; fair representation of persons with disabilities; universal suffrage based on the aspiration for fair representation and equality of vote.",
                List.of("elections", "democracy", "representation", "gender")),
            art(7, "Representation of the People",
                82, "Legislation on Elections",
                "Parliament shall enact legislation to provide for the conduct of elections and referenda and the regulation and efficient supervision of elections and referenda.",
                List.of("elections", "parliament", "legislation")),
            art(7, "Representation of the People",
                86, "Voting",
                "At every election, the Independent Electoral and Boundaries Commission shall ensure that voting is simple, accurate, verifiable, secure, accountable and transparent.",
                List.of("voting", "IEBC", "elections", "transparency")),

            // ── Chapter 8: The Legislature ───────────────────────────────────
            art(8, "The Legislature",
                93, "Establishment of Parliament",
                "There is established a Parliament of Kenya, which shall consist of the National Assembly and the Senate.",
                List.of("parliament", "legislature", "national assembly", "senate")),
            art(8, "The Legislature",
                94, "Role of Parliament",
                "The legislative authority of the Republic is derived from the people and, at the national level, is vested in and exercised by Parliament. Parliament manifests the diversity of the nation, represents the will of the people, and exercises their sovereignty.",
                List.of("parliament", "legislation", "sovereignty")),
            art(8, "The Legislature",
                95, "Role of the National Assembly",
                "The National Assembly represents the constituencies, and special interests in Kenya. The National Assembly deliberates on and resolves issues of concern to the people; enacts legislation; approves the Budget of the national government and its supplementary budgets.",
                List.of("national assembly", "legislation", "budget")),
            art(8, "The Legislature",
                96, "Role of the Senate",
                "The Senate represents the counties, and serves to protect the interests of the counties and their governments. The Senate shall debate and approve Bills concerning counties.",
                List.of("senate", "counties", "devolution")),

            // ── Chapter 9: The Executive ──────────────────────────────────────
            art(9, "The Executive",
                129, "Principles of Executive Authority",
                "Executive authority derives from the people of Kenya and shall be exercised in accordance with this Constitution. Executive authority shall be exercised in a manner compatible with the principle of service to the people of Kenya, and for their well-being and benefit.",
                List.of("executive", "president", "authority")),
            art(9, "The Executive",
                130, "The National Executive",
                "The national executive of the Republic comprises the President; the Deputy President; and the rest of the Cabinet.",
                List.of("executive", "president", "cabinet")),
            art(9, "The Executive",
                131, "Authority of the President",
                "The President is the Head of State and Government and the Commander-in-Chief of the Kenya Defence Forces. The President shall — respect, uphold and safeguard this Constitution; safeguard the sovereignty of the Republic; promote and enhance the unity of the nation; promote respect for the diversity of the people and communities of Kenya; and ensure the protection of human rights and fundamental freedoms and the rule of law.",
                List.of("president", "executive", "head of state", "commander in chief")),
            art(9, "The Executive",
                142, "Term of Office of the President",
                "The President shall hold office for a term beginning on the day the President is sworn in and ending when the person next elected President in accordance with Article 136(2)(a) is sworn in. A person shall not hold office as President for more than two terms.",
                List.of("president", "term", "two terms", "office")),

            // ── Chapter 10: Judiciary ─────────────────────────────────────────
            art(10, "Judiciary",
                159, "Judicial Authority",
                "Judicial authority is derived from the people and vests in, and shall be exercised by, the courts and tribunals established by or under this Constitution. In exercising judicial authority, the courts and tribunals shall be guided by the following principles: justice shall be done to all, irrespective of status; justice shall not be delayed; alternative forms of dispute resolution including reconciliation, mediation, arbitration and traditional dispute resolution mechanisms shall be promoted.",
                List.of("judiciary", "courts", "justice", "authority")),
            art(10, "Judiciary",
                160, "Independence of the Judiciary",
                "In the exercise of judicial authority, the Judiciary shall be subject only to this Constitution and the law and shall not be subject to the control or direction of any person or authority.",
                List.of("judiciary", "independence", "courts")),
            art(10, "Judiciary",
                163, "Supreme Court",
                "There is established the Supreme Court, which shall consist of the Chief Justice, who shall be the President of the Court; the Deputy Chief Justice; and five other judges.",
                List.of("supreme court", "chief justice", "judiciary")),
            art(10, "Judiciary",
                165, "High Court",
                "There is established the High Court, which shall consist of the number of judges prescribed by an Act of Parliament. The High Court has unlimited original jurisdiction in criminal and civil matters.",
                List.of("high court", "judiciary", "jurisdiction")),

            // ── Chapter 11: Devolved Government ──────────────────────────────
            art(11, "Devolved Government",
                174, "Objects of Devolution",
                "The objects of the devolution of government are to promote democratic and accountable exercise of power; foster national unity by recognising diversity; give powers of self-governance to the people and enhance their participation in the exercise of the powers of the State and in making decisions affecting them.",
                List.of("devolution", "counties", "democracy", "governance")),
            art(11, "Devolved Government",
                176, "County Governments",
                "There shall be a county government for each county, consisting of a county assembly and a county executive committee.",
                List.of("county government", "devolution", "counties")),
            art(11, "Devolved Government",
                179, "County Executive Committee",
                "The executive authority of the county is vested in, and exercised by, the county executive committee. The county executive committee consists of the county governor, who is the head of the county executive committee; the deputy county governor; and members appointed by the county governor.",
                List.of("county governor", "executive", "devolution")),

            // ── Chapter 12: Public Finance ────────────────────────────────────
            art(12, "Public Finance",
                201, "Principles of Public Finance",
                "The following principles shall guide all aspects of public finance in the Republic: there shall be openness and accountability, including public participation in financial matters; the public finance system shall promote an equitable society, and in particular: the burden of taxation shall be shared fairly; revenue raised nationally shall be shared equitably among national and county governments.",
                List.of("finance", "taxation", "budget", "accountability")),
            art(12, "Public Finance",
                209, "Power to Impose Taxes and Charges",
                "Only the national government may impose income tax; value-added tax; customs duties and other duties on import and export goods; excise tax.",
                List.of("taxation", "VAT", "customs", "finance")),

            // ── Chapter 13: The Public Service ───────────────────────────────
            art(13, "The Public Service",
                232, "Values and Principles of Public Service",
                "The values and principles of public service include high standards of professional ethics; efficient, effective and economic use of resources; responsive, prompt, effective, impartial and equitable provision of services; involvement of the people in the process of policy making; accountability for administrative acts; transparency and provision to the public of timely, accurate information.",
                List.of("public service", "ethics", "accountability", "transparency")),

            // ── Chapter 14: National Security ────────────────────────────────
            art(14, "National Security",
                238, "National Security",
                "National security is the protection against internal and external threats to Kenya's territorial integrity and sovereignty; its people, their rights, freedoms, property, peace, stability and prosperity; and its constitutional order.",
                List.of("security", "military", "national security")),
            art(14, "National Security",
                239, "Establishment of National Security Organs",
                "The national security organs are the Kenya Defence Forces; the National Intelligence Service; and the National Police Service.",
                List.of("KDF", "NIS", "police", "security organs")),

            // ── Chapter 15: Commissions and Independent Offices ──────────────
            art(15, "Commissions and Independent Offices",
                248, "Application of Chapter",
                "This Chapter applies to the commissions and independent offices established under this Constitution including the National Cohesion and Integration Commission and the Ethics and Anti-Corruption Commission.",
                List.of("commissions", "EACC", "independent offices")),
            art(15, "Commissions and Independent Offices",
                249, "Objects, Authority and Funding",
                "The objects of the commissions and the independent offices are to protect the sovereignty of the people; secure the observance by all State organs of democratic values and principles; and promote constitutionalism.",
                List.of("commissions", "sovereignty", "constitutionalism")),

            // ── Chapter 16: Amendment of This Constitution ────────────────────
            art(16, "Amendment of this Constitution",
                255, "Bills to Amend this Constitution",
                "A Bill to amend this Constitution may be introduced in either House of Parliament. A Bill to amend this Constitution shall have been passed by Parliament with the support of at least two thirds of all the members of the National Assembly, and at least two thirds of all the county delegations in the Senate.",
                List.of("amendment", "constitution", "parliament")),
            art(16, "Amendment of this Constitution",
                257, "Amendment by Popular Initiative",
                "A Bill to amend this Constitution may be introduced by a popular initiative signed by at least one million registered voters.",
                List.of("amendment", "popular initiative", "referendum")),

            // ── Chapter 17: General Provisions ───────────────────────────────
            art(17, "General Provisions",
                259, "Interpreting this Constitution",
                "This Constitution shall be interpreted in a manner that promotes its purposes, values and principles; advances the rule of law, and the human rights and fundamental freedoms in the Bill of Rights; permits the development of the law; and contributes to good governance.",
                List.of("interpretation", "constitution", "rule of law")),

            // ── Chapter 18: Transitional and Consequential Provisions ─────────
            art(18, "Transitional and Consequential Provisions",
                261, "Legislation Required by this Constitution",
                "Parliament shall, as soon as practicable after the effective date, enact legislation required by this Constitution.",
                List.of("transitional", "legislation", "implementation"))
        );

        articles.insertMany(seed);
        System.out.println("✓ Seeded " + seed.size() + " articles across 18 chapters.");
        DatabaseManager.getInstance().close();
    }

    private static Document art(int chapterNum, String chapterTitle,
                                 int articleNum, String title,
                                 String content, List<String> tags) {
        return new Document()
                .append("chapterNumber", chapterNum)
                .append("chapterTitle",  chapterTitle)
                .append("articleNumber", articleNum)
                .append("title",         title)
                .append("content",       content)
                .append("tags",          tags);
    }
}
